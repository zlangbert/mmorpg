package mmorpg

import org.scalajs.dom
import org.scalajs.dom.HTMLImageElement

import scala.collection.mutable
import scala.concurrent.Promise
import scalatags.JsDom.all._
import mmorpg.util.DelayedInit
import scala.scalajs.js

object Assets extends DelayedInit {

  register("tilesheet", "/maps/test/images/tilesheet.png")

  /**
   * Map from asset key to image
   */
  private lazy val assetData = mutable.Map[String, dom.HTMLImageElement]()

  private def register(key: String, path: String): Unit = {
    val image = img(src:=path).render
    val p = Promise[HTMLImageElement]()

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
}
