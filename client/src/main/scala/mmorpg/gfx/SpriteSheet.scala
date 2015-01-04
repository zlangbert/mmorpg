package mmorpg.gfx

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
   * @param tileId The index of the sprite
   * @return The sprite at the specified index
   */
  def apply(tileId: Int): Sprite = {
    spriteCache.getOrElseUpdate(tileId, {
      val tilesX = img.width / 48
      val offsetX = tileId % tilesX * DefaultSpriteConfig.tileSize
      val offsetY = tileId / tilesX * DefaultSpriteConfig.tileSize
      Sprite(img, DefaultSpriteConfig.copy(offsetX = offsetX, offsetY = offsetY))
    })
  }
}

object SpriteSheet {
  def apply(img: dom.HTMLImageElement) = new SpriteSheet(img)
}