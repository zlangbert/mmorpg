package mmorpg.gfx

trait Renderable {
  def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit
}