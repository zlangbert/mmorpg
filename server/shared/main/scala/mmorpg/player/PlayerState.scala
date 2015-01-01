package mmorpg.player

import java.util.UUID

import mmorpg.util.Vec

case class PlayerState(id: UUID, position: Vec)

object PlayerState {
  //waiting for upickle fix
  //def apply(id: UUID): PlayerState = PlayerState(id, Vec())
}