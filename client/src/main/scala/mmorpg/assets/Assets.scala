package mmorpg.assets

import mmorpg.assets.Asset.AssetType
import mmorpg.tmx.Tmx
import mmorpg.util.{DelayedInit, Logging}
import org.scalajs.dom

import scala.collection.mutable
import scala.concurrent.Promise
import scala.scalajs.js
import scalatags.JsDom.all._

object Assets extends DelayedInit with Logging {

  /**
   * Map from asset key to image
   */
  private val assetData = mutable.Map[String, dom.HTMLImageElement]()

  /**
   * Returns the html image associated with the asset. The
   * image is not necessarily done loading.
   * @param key The asset key
   * @return The image
   */
  def apply(key: String, `type`: AssetType): dom.HTMLImageElement = {
    val prefix = assetPrefix(`type`)
    val id = s"$prefix:$key"
    assetData.getOrElse(id,
      throw new Exception(s"tried to get an unregistered asset: $id"))
  }

  /**
   * Load the assets for a map
   * @param mapName The name of the map
   * @param map The map
   */
  def loadTilesets(mapName: String, map: Tmx.Map): Unit = {
    map.tilesets.foreach { tileset =>
      val key = tileset.name
      val path = s"/maps/$mapName/${tileset.imagePath}"
      register(key, path, Asset.Tileset)
    }
  }

  /**
   * Load a sprite sheet
   * @param key The sheet key
   */
  def loadSprite(key: String): Unit = {
    val path = s"/sprites/$key.png"
    register(key, path, Asset.Sprite)
  }

  /**
   * Register an asset
   * @param key Asset key
   * @param path Asset path
   */
  private def register(key: String, path: String, `type`: AssetType): Unit = {
    val prefix = assetPrefix(`type`)
    val fullKey = s"$prefix:$key"
    val image = img(src:=path).render
    assetData += fullKey -> image
    Log.debug(s"registering $fullKey -> $path")

    //resolve promise when the image is fully loaded
    val p = Promise[dom.HTMLImageElement]()
    image.asInstanceOf[js.Dynamic].onload = { () =>
      p.success(image)
    }
    waitFor(p.future)
  }

  private def assetPrefix(`type`: AssetType): String = `type` match {
    case Asset.Tileset => "tileset"
    case Asset.Sprite => "sprite"
  }
}
