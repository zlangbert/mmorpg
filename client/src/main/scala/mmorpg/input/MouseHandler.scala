package mmorpg.input

import org.scalajs.dom.{HTMLElement, MouseEvent}

class MouseHandler(target: HTMLElement) {

  var x = 0
  var y = 0

  target.onmousemove = { e: MouseEvent =>
    x = e.clientX.toInt
    y = e.clientY.toInt
  }
}

object MouseHandler {
  def apply(target: HTMLElement) = new MouseHandler(target)
}