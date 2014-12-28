package mmorpg

object Messages {

  case class Push(msg: Message)

  sealed trait Message
  case class StringMessage(s: String) extends Message
  case class Spawn(player: PlayerInfo) extends Message
  case class Despawn(player: PlayerInfo) extends Message
  case class Move(player: PlayerInfo) extends Message

  sealed trait ClientMessage
  case class MoveRequest(dir: Int) extends ClientMessage
}