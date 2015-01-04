package mmorpg

import mmorpg.stubs.Stats
import org.scalajs.dom
import org.scalajs.dom.KeyboardEvent

object DebugInfo {

  val fpsCounter = new Stats()
  fpsCounter.setMode(0)
  fpsCounter.domElement.style.position = "absolute"
  fpsCounter.domElement.style.top = "0px"
  fpsCounter.domElement.style.right = "0px"

  val frameTimeCounter = new Stats()
  frameTimeCounter.setMode(1)
  frameTimeCounter.domElement.style.position = "absolute"
  frameTimeCounter.domElement.style.top = "48px"
  frameTimeCounter.domElement.style.right = "0px"

  dom.document.onkeypress = { e: KeyboardEvent =>
    if (e.charCode == 96) {
      if (isHidden) show()
      else hide()
    }
  }

  def attach(parent: dom.HTMLElement): Unit = {
    parent.appendChild(fpsCounter.domElement)
    parent.appendChild(frameTimeCounter.domElement)
  }

  def frameStart(): Unit = {
    fpsCounter.begin()
    frameTimeCounter.begin()
  }

  def frameEnd(): Unit = {
    fpsCounter.end()
    frameTimeCounter.end()
  }

  def show(): Unit = {
    fpsCounter.domElement.style.display = "initial"
    frameTimeCounter.domElement.style.display = "initial"
  }

  def hide(): Unit = {
    fpsCounter.domElement.style.display = "none"
    frameTimeCounter.domElement.style.display = "none"
  }

  def isHidden: Boolean = {
    fpsCounter.domElement.style.display == "none"
  }
}
