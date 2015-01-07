package mmorpg.assets

import mmorpg.tmx.{Tmx, TmxLoader}

import scala.async.Async._
import scala.concurrent.Promise

object MapLoader extends AssetLoader[Tmx.Map] {

  override protected def load(key: AssetKey, promise: Promise[Asset]): Unit = {
    promise completeWith {
      async {
        val map = await(TmxLoader.load(key))
        map
      }
    }
  }
}