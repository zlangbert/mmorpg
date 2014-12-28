package mmorpg.messages

import mmorpg.PlayerInfo

object ServerMessage {

  case class Push(msg: ServerMessage)

  sealed trait ServerMessage
  case class StringMessage(s: String) extends ServerMessage
  case class Spawn(player: PlayerInfo) extends ServerMessage
  case class Despawn(player: PlayerInfo) extends ServerMessage
  case class Move(player: PlayerInfo) extends ServerMessage
}