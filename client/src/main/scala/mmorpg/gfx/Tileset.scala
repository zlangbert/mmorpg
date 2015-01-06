package mmorpg.gfx

import mmorpg.assets.{Asset, Assets}
import mmorpg.tmx.Tmx

import scala.collection.mutable

class Tileset(underlying: Tmx.Tileset) {

  /**
   * The tileset image
   */
  private lazy val img = Assets(underlying.name, Asset.Tileset)

  /**
   * Cache for created tiles
   */
  private val tileCache = mutable.Map[Int, Tile]()

  //load animated tiles into the cache
  loadAnimatedTiles()

  /**
   * Gets a Tile from this tileset by its global id
   * @param globalId The global id of the tile
   * @return The tile
   */
  def apply(globalId: Int): Tile = {
    val localId = globalId - firstGid
    tileCache.getOrElseUpdate(localId, {
      new StaticTile(localId, img, this)
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

  /**
   * The width of the tileset image in pixels
   * @return Image width in pixels
   */
  def imageWidth: Int = underlying.imageWidth

  private def loadAnimatedTiles(): Unit = {
    underlying.tileInfos foreach { info =>
      val id = info.id
      tileCache += id -> new AnimatedTile(id, img, this, info.animations)
    }
  }
}

object Tileset {
  def apply(underlying: Tmx.Tileset) = new Tileset(underlying)
}