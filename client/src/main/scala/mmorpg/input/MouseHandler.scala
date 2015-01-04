package mmorpg.input

import org.scalajs.dom.{HTMLElement, MouseEvent}

import scala.collection.mutable

class MouseHandler(target: HTMLElement) {

  type Callback = MouseEvent => Unit

  var x = 0
  var y = 0

  /**
   * List of registered click handlers
   */
  private val clickHandlers = mutable.Buffer[Callback]()
  target.onclick = { e: MouseEvent =>
    clickHandlers.foreach(_(e))
  }

  /**
   * Register a callback for clicks
   * @param f The callback
   */
  def onClick(f: Callback): Unit = clickHandlers += f

  /**
   * List of registered move handlers
   */
  private val moveHandlers = mutable.Buffer[Callback]()
  target.onmousemove = { e: MouseEvent =>
    moveHandlers.foreach(_(e))
  }

  /**
   * Register a callback for mouse movement
   * @param f The callback
   */
  def onMove(f: Callback): Unit = moveHandlers += f

  // Setup internal mouse position tracking
  onMove { e: MouseEvent =>
    x = e.clientX.toInt
    y = e.clientY.toInt
  }
}

object MouseHandler {
  def apply(target: HTMLElement) = new MouseHandler(target)
}