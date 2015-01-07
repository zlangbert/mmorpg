package mmorpg.assets

import mmorpg.util.{DelayedInit, Logging}

import scala.collection.mutable
import scala.concurrent.{Future, Promise}

trait AssetLoader[T] extends DelayedInit with Logging {

  type AssetKey = String
  type Asset = T

  implicit val ec = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
  protected val cache = mutable.Map[AssetKey, Future[Asset]]()

  def apply(key: AssetKey): Future[Asset] = {
    Log.debug(s"${getClass.getSimpleName}: getting $key")
    cache.getOrElseUpdate(key, {
      val promise = Promise[Asset]()
      load(key, promise)
      promise.future
    })
  }

  protected def load(key: AssetKey, promise: Promise[Asset]): Unit
}