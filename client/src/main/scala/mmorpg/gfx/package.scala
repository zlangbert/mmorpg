package mmorpg

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.extensions.Color

package object gfx {

  type RenderingContext = CanvasRenderingContext2D

  implicit class ContextOps(ctx: RenderingContext) {
    def clear(): Unit = {
      ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }

    def clear(color: Color): Unit = {
      ctx.fillStyle = color.toString()
      ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
    }
  }
}