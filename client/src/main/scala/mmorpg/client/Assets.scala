package mmorpg.client

import org.scalajs.dom

import scala.collection.mutable
import scalatags.JsDom.all._

object Assets {

  private val EmptyImage = img(src:="//:0").render
  EmptyImage.onloadeddata = { e: dom.Event => println("loaded") }

  private val assetData = mutable.Map[String, dom.HTMLImageElement]()

  def init(): Unit = ()

  def apply(key: String): dom.HTMLImageElement = {
    assetData.getOrElse(key, EmptyImage)
  }

  def register(key: String, data: String) = {
    val image = dom.extensions.Image.createBase64Svg(data)
    assetData += key -> image
  }
}