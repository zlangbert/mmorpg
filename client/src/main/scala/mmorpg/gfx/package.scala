package mmorpg

import org.scalajs.dom.CanvasRenderingContext2D

package object gfx {

  type RenderingContext = CanvasRenderingContext2D

  implicit class ContextOps(ctx: RenderingContext) {
    def clear(): Unit = {
      ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }
  }
}