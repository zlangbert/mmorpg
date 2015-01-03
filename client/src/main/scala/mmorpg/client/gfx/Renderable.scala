package mmorpg.client.gfx

import org.scalajs.dom

trait Renderable {
  def renderAt(ctx: dom.CanvasRenderingContext2D, x: Int, y: Int): Unit
}