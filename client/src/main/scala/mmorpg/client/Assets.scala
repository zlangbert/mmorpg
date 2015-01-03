package mmorpg.client

import org.scalajs.dom

import scala.collection.mutable
import org.scalajs.dom
import scalatags.JsDom.all._

object Assets {

  /**
   * Map from asset key to image
   */
  private val assetData = mutable.Map[String, dom.HTMLImageElement]()

  /**
   * onReady callbacks
   */
  private val callbacks = mutable.Buffer[() => Unit]()

  assetData += "tilesheet" -> img(src:="/tilesheet.png").render

  val readyCheck: Int = dom.setInterval(() => {
    if (assetData.contains("tilesheet") && assetData("tilesheet").complete) {
      callbacks.foreach(_())
      dom.clearInterval(readyCheck)
    }
  }, 50)

  def apply(key: String): dom.HTMLImageElement = {
    assetData(key)
  }

  def onReady(f: () => Unit): Unit = {
    callbacks += f
  }
}