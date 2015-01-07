package mmorpg.assets

import mmorpg.gfx.sprites.Sprite
import mmorpg.gfx.sprites.Sprite.SpriteData
import org.scalajs.dom.extensions._

import scala.async.Async._
import scala.concurrent.{Future, Promise}

object SpriteLoader extends AssetLoader[Sprite] {

  private val base = "/sprites/"

  protected def path(key: AssetKey): String = base + key

  override protected def load(key: AssetKey, promise: Promise[Asset]): Unit = {
    promise completeWith {
      async {
        val img = await(ImageLoader(path(key) + ".png"))
        val data = await(loadData(key))
        Sprite(key, img, data)
      }
    }
  }

  private def loadData(k: AssetKey): Future[SpriteData] = {
    Ajax.get(path(k) + ".json") map { xhr =>
      upickle.read[SpriteData](xhr.responseText)
    }
  }

  /**
   * A list of all sprite keys
   */
  lazy val spriteKeys: Future[Seq[String]] = {
    Ajax.get("/sprites").map {
      xhr => upickle.read[Seq[String]](xhr.responseText)
    }
  }
}