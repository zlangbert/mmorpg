package mmorpg.gfx.animations

import mmorpg.gfx.TimeDelta

trait Animation {

  private var _time = 0.0

  protected def time: Double = _time
  protected def time_=(d: Double): Unit = _time = d

  /**
   * Updates the animation using the given delta
   * @param delta Frame time delta
   */
  def update(delta: TimeDelta): Unit = time += delta
}