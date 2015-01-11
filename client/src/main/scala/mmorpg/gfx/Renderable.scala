package mmorpg.gfx

trait Renderable {
  def render()(implicit delta: TimeDelta, ctx: RenderingContext): Unit
}