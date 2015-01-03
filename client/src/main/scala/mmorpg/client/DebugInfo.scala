package mmorpg.client

import mmorpg.client.stubs.Stats
import org.scalajs.dom

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
}