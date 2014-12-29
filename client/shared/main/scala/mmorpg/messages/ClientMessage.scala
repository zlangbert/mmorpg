package mmorpg.messages

import mmorpg.util.Direction.Direction

object ClientMessage {

  sealed trait ClientMessage
  case class MoveRequest(dir: Direction) extends ClientMessage
}