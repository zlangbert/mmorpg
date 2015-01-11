package mmorpg.gfx

import mmorpg.gfx.tiles.Tileset
import mmorpg.tmx.Tmx

import scala.collection.mutable

class TmxRenderer(map: Tmx.Map, tilesets: Seq[Tileset]) {

  def render(camera: Camera)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    camera.forEachVisibleTile { index =>
      /*map.getStack(index).foreach { gid =>
        renderTile(gid, index, camera)
      }*/
    }
  }

  private def renderTile(gid: Int, index: Int, camera: Camera)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    if (isEmptyTile(gid)) return
    val tileset = tilesetByGid(gid)
    val tile = tileset(gid)
    val x = (index % map.width * tileset.tileSize) - (camera.position.x - ctx.canvas.width / 2)
    val y = (index / map.width * tileset.tileSize) - (camera.position.y - ctx.canvas.height / 2)
    tile.renderAt(x, y)
  }

  private val tilesetGidCache = mutable.Map[Int, Tileset]()
  private def tilesetByGid(gid: Int): Tileset = {
    tilesetGidCache.getOrElseUpdate(gid, {
      tilesets.reverse.dropWhile(_.firstGid > gid).head
    })
  }

  private def isEmptyTile(tileId: Int) = tileId == 0
}