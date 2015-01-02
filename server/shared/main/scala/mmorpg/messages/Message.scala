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
    def id: UUID
  }
  case class InitializeClient(id: UUID) extends Message
  case class Spawn(id: UUID, state: PlayerState) extends Message
  case class Move(id: UUID, direction: Direction) extends Message
  case class UpdateState(id: UUID, state: PlayerState) extends Message
}