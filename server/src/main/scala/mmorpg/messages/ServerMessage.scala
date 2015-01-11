package mmorpg.messages

import java.util.UUID

import akka.actor.ActorRef
import mmorpg.messages.Message.Message

object ServerMessage {

  sealed trait ServerMessage

  case object Tick extends ServerMessage

  case class Push(msg: Message) extends ServerMessage
  case class Broadcast(msg: Message) extends ServerMessage
  case class BroadcastPush(msg: Message) extends ServerMessage

  case class ClientConnected(id: UUID, connection: ActorRef) extends ServerMessage
  case class ClientDisconnected(id: UUID) extends ServerMessage

  case object AnnounceSpawn

  case class MoveRequest(id: UUID, x: Int, y: Int) extends ServerMessage
}