package mmorpg.messages

import java.util.UUID

import mmorpg.player.PlayerState
import mmorpg.util.Direction.Direction
import upickle.Js

object Message {

  implicit val uuidWriter = upickle.Writer[UUID] {
    case id => Js.Str(id.toString)
  }
  implicit val uuidReader = upickle.Reader[UUID] {
    case Js.Str(s) => UUID.fromString(s)
  }

  sealed trait Message {
    def target: UUID
  }
  case class InitializeClient(target: UUID) extends Message
  case class Spawn(target: UUID, state: PlayerState) extends Message
  case class Despawn(target: UUID) extends Message
  case class Move(target: UUID, direction: Direction) extends Message
  case class UpdateState(target: UUID, state: PlayerState) extends Message
}