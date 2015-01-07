package mmorpg.gfx.tiles

import mmorpg.gfx._
import mmorpg.gfx.animations.TileAnimation
import mmorpg.tmx.Tmx
import org.scalajs.dom.HTMLImageElement

/**
 * An animated tile that renders a repeating animation based on its animation sequence
 * @param localId The local id of this tile
 * @param img The tileset image
 * @param tileset The tileset this tile belongs to
 * @param sequence The animation sequence
 */
case class AnimatedTile(localId: Int, img: HTMLImageElement, tileset: Tileset, sequence: Seq[Tmx.TileAnimation])
  extends Tile {

  val animation = TileAnimation(sequence)

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    animation.update(delta)
    val offset = getOffset(animation.tileId)
    if (x >= ctx.canvas.width || y >= ctx.canvas.height) return
    ctx.drawImage(img, offset.x, offset.y, tileset.tileSize, tileset.tileSize,
      x, y, tileset.tileSize, tileset.tileSize)
  }
}