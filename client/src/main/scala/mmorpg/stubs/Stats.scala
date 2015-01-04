package mmorpg.stubs

import org.scalajs.dom.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@JSName("Stats")
class Stats extends js.Object {

  val domElement: HTMLElement = js.native

  def setMode(m: Int): Unit = js.native
  def begin(): Unit = js.native
  def end(): Unit = js.native
}