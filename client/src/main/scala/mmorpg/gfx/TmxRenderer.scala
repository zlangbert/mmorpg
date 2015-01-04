package mmorpg.gfx

import mmorpg.Assets
import mmorpg.tmx.Tmx
import org.scalajs.dom.CanvasRenderingContext2D

class TmxRenderer(map: Tmx.Map) extends Renderable {

  val tilesheet = SpriteSheet(Assets("tilesheet"))

  override def renderAt(x: Int, y: Int)(implicit ctx: CanvasRenderingContext2D): Unit = {
    map.layers.foreach { layer =>
      layer.data.zipWithIndex.filterNot(_._1 == 0).foreach { case (tileId, index) =>
        val tile = tilesheet(tileId-1)
        val x = index % map.width * 48
        val y = index / map.width * 48
        tile.renderAt(x, y)
      }
    }
  }
}