package mmorpg

import mmorpg.tmx.Tmx
import org.scalajs.dom

import scala.collection.mutable
import scala.concurrent.Promise
import scalatags.JsDom.all._
import mmorpg.util.{Logging, DelayedInit}
import scala.scalajs.js

object Assets extends DelayedInit with Logging {

  /**
   * Map from asset key to image
   */
  private lazy val assetData = mutable.Map[String, dom.HTMLImageElement]()

  private def register(key: String, path: String): Unit = {
    Log.debug(s"registering $key -> $path")
    val image = img(src:=path).render
    val p = Promise[dom.HTMLImageElement]()

    //resolve promise when the image is fully loaded
    image.asInstanceOf[js.Dynamic].onload = { () =>
      p.success(image)
    }
    waitFor(p.future)

    assetData += key -> image
  }

  /**
   * Returns the html image associated with the asset. The
   * image is not necessarily done loading.
   * @param key The asset key
   * @return The image
   */
  def apply(key: String): dom.HTMLImageElement = assetData(key)

  def load(mapName: String, map: Tmx.Map): Unit = {
    map.tilesets.foreach { tileset =>
      val key = tileset.name
      val path = s"/maps/$mapName/${tileset.imagePath}"
      register(key, path)
    }
  }
}
