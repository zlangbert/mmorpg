package mmorpg.gfx.animation

class SpriteAnimation extends Animation {

  def tileId: Int = 40
}

object SpriteAnimation {
  def apply() = new SpriteAnimation
}