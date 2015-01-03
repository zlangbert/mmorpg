package mmorpg.client.gfx

import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D

private[gfx] class Sprite(img: dom.HTMLImageElement)(implicit spriteConfig: SpriteConfig) extends Renderable {

  override def renderAt(ctx: CanvasRenderingContext2D, x: Int, y: Int): Unit = {
    ctx.drawImage(img,
      spriteConfig.offsetX, spriteConfig.offsetY,
      spriteConfig.tileSize, spriteConfig.tileSize,
      x, y, spriteConfig.tileSize, spriteConfig.tileSize)
  }
}

object Sprite {
  def apply(image: dom.HTMLImageElement, spriteConfig: SpriteConfig) = new Sprite(image)(spriteConfig)
}