package mmorpg.gfx.tiles

import mmorpg.gfx._
import org.scalajs.dom.HTMLImageElement

/**
 * A static tile that simply renders a piece of the tileset based
 * @param localId The local tile id
 * @param img The tileset image
 * @param tileset The tileset this tile belongs to
 */
case class StaticTile(localId: Int, img: HTMLImageElement, tileset: Tileset) extends Tile {

  val offset = getOffset(localId)

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {

    //this will go away when a proper camera is implemented and only visible tiles are rendered
    if (x >= ctx.canvas.width || y >= ctx.canvas.height) return

    ctx.drawImage(img, offset.x, offset.y, tileset.tileSize, tileset.tileSize,
      x, y, tileset.tileSize, tileset.tileSize)
  }
}