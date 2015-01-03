package mmorpg.client.gfx

import org.scalajs.dom
import scala.collection.mutable

class SpriteSheet(img: dom.HTMLImageElement) {

  private val DefaultSpriteConfig = SpriteConfig(48, 0, 0)

  /**
   * Cache created sprites
   */
  private val spriteCache = mutable.Map[Int, Sprite]()

  /**
   * Gets the sprite at an index. Sprites are
   * created as needed
   * @param index The index of the sprite
   * @return The sprite at the specified index
   */
  def apply(index: Int): Sprite = {
    spriteCache.getOrElseUpdate(index, {
      val tilesX = img.width / 48
      val offsetX = index % tilesX * DefaultSpriteConfig.tileSize
      val offsetY = index / tilesX * DefaultSpriteConfig.tileSize
      Sprite(img, DefaultSpriteConfig.copy(offsetX = offsetX, offsetY = offsetY))
    })
  }
}

object SpriteSheet {
  def apply(img: dom.HTMLImageElement) = new SpriteSheet(img)
}