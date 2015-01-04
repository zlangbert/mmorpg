package mmorpg.util

import scala.collection.mutable
import scala.concurrent.Future

trait DelayedInit {

  private val ops = mutable.Buffer[Future[_]]()

  protected def waitFor[T](f: Future[T]): Future[T] = {
    ops += f
    f
  }

  def isReady: Boolean = ops.forall(_.isCompleted)
}