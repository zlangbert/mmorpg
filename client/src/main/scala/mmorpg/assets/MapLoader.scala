package mmorpg.assets

import mmorpg.tmx.{Tmx, TmxLoader}

import scala.concurrent.Promise

object MapLoader extends AssetLoader[Tmx.Map] {

  override protected def load(key: AssetKey, promise: Promise[Asset]): Unit = {
    promise completeWith {
      TmxLoader.load(key)
    }
  }
}