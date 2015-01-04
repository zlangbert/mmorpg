package mmorpg.tmx

import org.scalajs.dom.extensions.Ajax

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object PlatformTmxLoader {

  def load(key: String): Future[Tmx.Map] = {
    val path = s"/maps/$key/data"
    Ajax.get(path).map { case xhr =>
      val res = xhr.responseText
      val loader = new TmxJsonLoader
      loader.load(res)
    }
  }
}