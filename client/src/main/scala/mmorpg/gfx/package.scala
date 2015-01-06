package mmorpg

import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.extensions.Color

import scala.language.implicitConversions

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

  class TimeDelta(val underlying: Double) extends AnyVal

  implicit def double2TimeDelta(d: Double): TimeDelta = new TimeDelta(d)
  implicit def timeDelta2Double(d: TimeDelta): Double = d.underlying
}