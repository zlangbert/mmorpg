package mmorpg.gfx

import mmorpg.util.Vec

class Camera(size: Vec) {

  type Position = Vec

  var _position: Position = Vec(15*48,15*48)

  def position = _position
  def position_(p: Position) = _position = p

  def forEachVisibleTile(f: Int => Unit): Unit = {
    for {
      x <- 0 until 32//position.x until position.x + size.x if x < 32
      y <- 0 until 32//position.y until position.y + size.y if y < 32
    } {
      val index = y * 32 + x
      f(index)
    }
  }
}