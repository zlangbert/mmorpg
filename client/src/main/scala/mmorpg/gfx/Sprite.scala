package mmorpg.gfx

import org.scalajs.dom

class Sprite(img: dom.HTMLImageElement) extends Renderable {

  val animationState = new Animation

  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    val (ox, oy) = animationState.offset
    ctx.drawImage(img, ox, oy, 96, 96, x-48, y-72, 96, 96)
  }
}

object Sprite {
  def apply(img: dom.HTMLImageElement) = new Sprite(img)
}