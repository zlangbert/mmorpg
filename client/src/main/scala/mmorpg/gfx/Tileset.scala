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
   * Gets a Tile from this tileset by its global id
   * @param globalId The global id of the tile
   * @return The tile
   */
  def apply(globalId: Int): Tile = {
    val id = globalId - firstGid
    tileCache.getOrElseUpdate(id, {
      val tilesX = underlying.imageWidth / tileSize
      val offsetX = id % tilesX * tileSize
      val offsetY = id / tilesX * tileSize
      new Tile(id, img, this, Vec(offsetX, offsetY))
    })
  }

  /**
   * The first global id for this tileset
   * @return
   */
  def firstGid: Int = underlying.firstGid

  /**
   * The size of a tile in pixels
   * @return Tile size in pixels
   */
  def tileSize: Int = underlying.tileWidth
}

object Tileset {
  def apply(underlying: Tmx.Tileset) = new Tileset(underlying)
}