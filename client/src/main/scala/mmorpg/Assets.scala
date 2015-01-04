package mmorpg

import org.scalajs.dom

import scala.collection.mutable
import scalatags.JsDom.all._

object Assets {

  register("tilesheet", "/maps/test/images/tilesheet.png")

  /**
   * Map from asset key to image
   */
  private lazy val assetData = mutable.Map[String, dom.HTMLImageElement]()

  private def register(key: String, path: String): Unit = {
    assetData += key -> img(src:=path).render
  }

  /**
   * onReady callbacks
   */
  private val callbacks = mutable.Buffer[() => Unit]()

  /**
   * Schedules a check to make sure images are loaded
   * and fies callbacks when ready
   */
  private val readyCheck: Int = dom.setInterval(() => {
    if (assetData.contains("tilesheet") && assetData("tilesheet").complete) {
      callbacks.foreach(_())
      dom.clearInterval(readyCheck)
    }
  }, 50)

  /**
   * Returns the html image associated with the asset.
   * Throws an exception if the asset is not done loading.
   * @param key The asset key
   * @return The image
   */
  def apply(key: String): dom.HTMLImageElement = assetData(key)

  /**
   * Register a callback to be fired when all assets are done loading
   * @param f The callback
   */
  def onReady(f: () => Unit): Unit = callbacks += f
}
