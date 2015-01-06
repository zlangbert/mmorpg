package mmorpg.gfx

import mmorpg.gfx.animation.SpriteAnimation
import mmorpg.util.Vec
import org.scalajs.dom

class Sprite(img: dom.HTMLImageElement) extends Renderable {

  val animation = SpriteAnimation()

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    animation.update(delta)
    val offset = getOffset(animation.tileId)
    ctx.drawImage(img, offset.x, offset.y, 96, 96, x-48, y-72, 96, 96)
  }

  private def getOffset(localId: Int): Vec = {
    val tilesX = 5
    val offsetX = localId % tilesX * 96
    val offsetY = localId / tilesX * 96
    Vec(offsetX, offsetY)
  }
}

object Sprite {
  def apply(img: dom.HTMLImageElement) = new Sprite(img)
}