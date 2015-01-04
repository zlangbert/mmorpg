package mmorpg.gfx

import org.scalajs.dom

trait Renderable {
  def renderAt(x: Int, y: Int)(implicit ctx: dom.CanvasRenderingContext2D): Unit
}