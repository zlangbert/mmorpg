package mmorpg.util

import scala.collection.mutable
import scala.concurrent.Future
import org.scalajs.dom

trait DelayedInit {

  private val ops = mutable.Buffer[Future[_]]()

  protected def waitFor[T](f: Future[T]): Future[T] = {
    ops += f
    f
  }

  def isReady: Boolean = ops.forall(_.isCompleted)
}

object DelayedInit {

  private var id = 0

  /**
   *
   * @param xs The objects to wait for
   * @param onReady A callback executed when all objects
   *                are ready
   * @return
   */
  def waitFor(xs: DelayedInit*)(onReady: => Unit): Unit = {
    if (id != 0) throw new Exception("waitFor already called")
    id = dom.setInterval({ () =>
      if (xs.forall(_.isReady)) {
        onReady
        dom.clearInterval(id)
      }
    }, 50)
  }
}