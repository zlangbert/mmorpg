package mmorpg.gfx

import mmorpg.util.Vec
import org.scalajs.dom.HTMLImageElement

class Tile(id: Int, img: HTMLImageElement, tileset: Tileset, offset: Vec) extends Renderable {
  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    ctx.drawImage(img, offset.x, offset.y, tileset.tileSize, tileset.tileSize,
      x, y, tileset.tileSize, tileset.tileSize)
  }
}