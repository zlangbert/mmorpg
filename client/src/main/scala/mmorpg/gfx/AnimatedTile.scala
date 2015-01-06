package mmorpg.gfx

import mmorpg.tmx.Tmx
import mmorpg.util.Vec
import org.scalajs.dom.HTMLImageElement

class AnimatedTile(id: Int, img: HTMLImageElement, tileset: Tileset, offset: Vec, animations: Seq[Tmx.TileAnimation])
  extends Tile(id, img, tileset, offset) {

  var animationIndex = 0
  var animationTime = 0

  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    val animation = animations(animationIndex)
    val offset = tileset.getOffset(animation.tileid)
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