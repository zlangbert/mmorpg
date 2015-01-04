package mmorpg

import org.scalajs.dom.CanvasRenderingContext2D

package object gfx {

  implicit class ContextOps(ctx: CanvasRenderingContext2D) {
    def clear(): Unit = {
      ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }
  }
}