package mmorpg.gfx

import mmorpg.Client
import org.scalajs.dom.extensions.Color

class TileOutliner extends Renderable {

  val ValidColor = Color("#FFDF7D").toString()
  val InvalidColor = Color("#DE1028").toString()
  val LineWidth = 3

  override def render()(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    ctx.strokeStyle = ValidColor
    ctx.lineWidth = LineWidth
    val (x, y) = Client.world.camera.clampToGrid(Client.mouseHandler.x, Client.mouseHandler.y)
    ctx.strokeRect(x, y, 48, 48)
  }
}