package mmorpg.player

import java.util.UUID

import mmorpg.util.Direction.Direction
import mmorpg.util.{Direction, Vec}

case class PlayerState(id: UUID, var position: Vec) {

  def move(dir: Direction): Unit = {
    position = dir match {
      case Direction.Up => position.copy(y = position.y + -2)
      case Direction.Down => position.copy(y = position.y + 2)
      case Direction.Left => position.copy(x = position.x + -2)
      case Direction.Right => position.copy(x = position.x + 2)
    }
  }
}

object PlayerState {
  //waiting for upickle fix
  //def apply(id: UUID): PlayerState = PlayerState(id, Vec())
}