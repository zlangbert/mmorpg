package mmorpg.gfx.sprites

import mmorpg.gfx._
import mmorpg.gfx.animations.SpriteAnimation
import mmorpg.gfx.sprites.Sprite.SpriteData
import mmorpg.util.Vec
import org.scalajs.dom

class Sprite(val key: String, img: dom.HTMLImageElement, data: SpriteData) extends RenderableAt {

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

  /**
   * Holds additional data related to the sprite
   * @param id
   * @param animations
   */
  case class SpriteData(id: String, animations: Map[String, SpriteAnimationData])

  /**
   * Data for a specific animation
   * @param length
   * @param row
   */
  case class SpriteAnimationData(length: Int, row: Int)

  def apply(key: String, img: dom.HTMLImageElement, data: SpriteData) = new Sprite(key, img, data)
}