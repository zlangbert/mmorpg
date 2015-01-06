package mmorpg.gfx

import mmorpg.tmx.Tmx
import org.scalajs.dom.HTMLImageElement

/**
 * An animated tile that renders a repeating animation based on its animation sequence
 * @param globalId The globalId of this tile
 * @param img The tileset image
 * @param tileset The tileset this tile belongs to
 * @param animations The animation sequence
 */
class AnimatedTile(globalId: Int, img: HTMLImageElement, tileset: Tileset, animations: Seq[Tmx.TileAnimation])
  extends Tile {

  var animationIndex = 0
  var animationTime = 0

  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    val animation = animations(animationIndex)
    val offset = tileset.getOffset(animation.tileId)
    if (x >= ctx.canvas.width || y >= ctx.canvas.height) return
    ctx.drawImage(img, offset.x, offset.y, tileset.tileSize, tileset.tileSize,
      x, y, tileset.tileSize, tileset.tileSize)

    animationTime += 1
    if (animationTime >= 12) {
      animationTime = 0
      if (animationIndex >= animations.length-1) {
        animationIndex = 0
      } else {
        animationIndex += 1
      }
    }
  }
}