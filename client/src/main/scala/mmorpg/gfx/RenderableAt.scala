package mmorpg.gfx

trait RenderableAt {
  def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit
}