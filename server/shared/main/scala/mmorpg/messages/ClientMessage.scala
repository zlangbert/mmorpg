package mmorpg.messages

object ClientMessage {

  sealed trait ClientMessage
  case class MoveRequest(dir: Int) extends ClientMessage
}