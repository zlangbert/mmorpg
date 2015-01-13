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
        val tileData = l.tileData(index)
        val (screenX, screenY) = camera.worldToScreen(worldX, worldY)
        renderTile(tileData, screenX, screenY)
      }
    }
  }

  private def renderTile(tileData: Tmx.TileLayer.TileData, x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    if (isEmptyTile(tileData.gid)) return
    val tileset = tilesetByGid(tileData.gid)
    val tile = tileset(tileData.gid)
    tile.renderAt(x, y)
  }

  private val tilesetGidCache = mutable.Map[Int, Tileset]()
  private def tilesetByGid(gid: Int): Tileset = {
    tilesetGidCache.getOrElseUpdate(gid, {
      tilesets.reverse.dropWhile(_.firstGid > gid).head
    })
  }

  private def isEmptyTile(gid: Int) = gid == 0
}