package mmorpg.gfx.tiles

import mmorpg.gfx.Renderable
import mmorpg.util.Vec

/**
 * All tile types should extends Tile
 */
trait Tile extends Renderable {

  /**
   * The tileset this tile belongs to
   * @return The tileset
   */
  def tileset: Tileset

  /**
   * Calculates the pixel offset of a tile on
   * this tileset
   * @param localId The local tile id
   * @return A Vec with the offset information
   */
  def getOffset(localId: Int): Vec = {
    val tilesX = tileset.imageWidth / tileset.tileSize
    val offsetX = localId % tilesX * tileset.tileSize
    val offsetY = localId / tilesX * tileset.tileSize
    Vec(offsetX, offsetY)
  }
}