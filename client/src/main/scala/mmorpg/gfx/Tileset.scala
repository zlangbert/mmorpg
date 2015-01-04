package mmorpg.gfx

import mmorpg.Assets
import mmorpg.tmx.Tmx
import mmorpg.util.Vec

import scala.collection.mutable

class Tileset(underlying: Tmx.Tileset) {

  /**
   * The tileset image
   */
  private lazy val img = Assets(underlying.name)

  /**
   * Cache for created tiles
   */
  private val tileCache = mutable.Map[Int, Tile]()

  /**
   *
   * @param id The id of the tile in this tileset
   * @return The tile
   */
  def apply(id: Int): Tile = {
    tileCache.getOrElseUpdate(id, {
      val tilesX = underlying.imageWidth / tileSize
      val offsetX = id % tilesX * tileSize
      val offsetY = id / tilesX * tileSize
      new Tile(id, img, this, Vec(offsetX, offsetY))
    })
  }

  /**
   * The size of a tile in pixels
   * @return Tile size in pixels
   */
  def tileSize: Int = underlying.tileWidth
}

object Tileset {
  def apply(underlying: Tmx.Tileset) = new Tileset(underlying)
}