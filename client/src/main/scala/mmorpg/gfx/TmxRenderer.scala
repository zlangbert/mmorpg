package mmorpg.gfx

import mmorpg.tmx.Tmx

class TmxRenderer(map: Tmx.Map) extends Renderable {

  val tileset = Tileset(map.tilesets.head)

  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    map.layers.filter(_.visible).foreach { layer =>
      layer.data.zipWithIndex
        .filterNot { case (tileId, _) => isEmptyTile(tileId) }
        .foreach(render)
    }
  }

  private def render(t: (Int, Int))(implicit ctx: RenderingContext): Unit = {
    val (tileId, index) = t
    val tile = tileset(tileId-1)
    val x = index % map.width * 48
    val y = index / map.width * 48
    tile.renderAt(x, y)
  }

  private def isEmptyTile(tileId: Int) = tileId == 0
}