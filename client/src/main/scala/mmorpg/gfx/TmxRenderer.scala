package mmorpg.gfx

import mmorpg.gfx.tiles.Tileset
import mmorpg.tmx.Tmx

import scala.collection.mutable

class TmxRenderer(map: Tmx.Map, tilesets: Seq[Tileset]) {

  def render(camera: Camera)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    map.tileLayers foreach { l =>
      if (!l.visible) return
      for (worldX <- 0 until camera.size.x by map.tileSize; worldY <- 0 until camera.size.y by map.tileSize) {
        val index = map.indexFromCoords(worldX, worldY)
        val gid = l.data(index)
        val (screenX, screenY) = camera.worldToScreen(worldX, worldY)
        renderTile(gid, screenX, screenY)
      }
    }
  }

  private def renderTile(gid: Int, x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    if (isEmptyTile(gid)) return
    val tileset = tilesetByGid(gid)
    val tile = tileset(gid)
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