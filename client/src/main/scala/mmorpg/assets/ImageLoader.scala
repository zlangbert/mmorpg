package mmorpg.assets

import org.scalajs.dom.HTMLImageElement

import scala.concurrent.Promise
import scala.scalajs.js
import scalatags.JsDom.all._

object ImageLoader extends AssetLoader[HTMLImageElement] {

  override protected def load(key: AssetKey, promise: Promise[Asset]): Unit = {
    val image = img(src:=key).render
    image.asInstanceOf[js.Dynamic].onload = { () =>
      promise.success(image)
    }
  }
}