package mmorpg.player

import java.util.UUID

import mmorpg.util.Vec

case class PlayerState(id: UUID, var position: Vec) {

  def moveTo(x: Int, y: Int): Unit = {
    position = Vec(x, y)
  }
}

object PlayerState {
  //waiting for upickle fix
  //def apply(id: UUID): PlayerState = PlayerState(id, Vec())
}