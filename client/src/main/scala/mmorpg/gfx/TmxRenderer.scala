package mmorpg.gfx

import mmorpg.gfx.tiles.Tileset
import mmorpg.tmx.Tmx

class TmxRenderer(map: Tmx.Map, tilesets: Seq[Tileset]) extends Renderable {

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    map.layers.filter(_.visible).foreach { layer =>
      layer.data.zipWithIndex
        .filterNot { case (gid, _) => isEmptyTile(gid) }
        .foreach(renderTile)
    }
  }

  private def renderTile(t: (Int, Int))(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    val (gid, index) = t
    val tileset = tilesetByGid(gid)
    val tile = tileset(gid)
    val x = index % map.width * tileset.tileSize
    val y = index / map.width * tileset.tileSize
    tile.renderAt(x, y)
  }

  //TODO: optimize this
  private def tilesetByGid(gid: Int): Tileset = {
    tilesets.reverse.dropWhile(_.firstGid > gid).head
  }

  private def isEmptyTile(tileId: Int) = tileId == 0
}