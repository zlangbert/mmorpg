package mmorpg.player

import akka.actor.{PoisonPill, Props, Actor}
import mmorpg.messages.Message.Message
import mmorpg.messages.ServerMessage._

class PlayerManagerActor extends Actor {

  override def receive: Receive = {

    /*
     * Create a player actor on connect
     */
    case ClientConnected(id, connection) =>
      context.actorOf(PlayerActor.props(id, connection), s"$id")

    /*
     * Kill player actor on disconnect
     */
    case ClientDisconnected(id) =>
      context.actorSelection(s"$id") ! PoisonPill

    case Broadcast(msg) =>
      context.actorSelection("*") ! msg

    /*
     * Forward any other message to the matching player
     */
    case msg: Message =>
      val id = ??? //msg.id
      context.actorSelection(s"$id") ! msg
  }
}

object PlayerManagerActor {
  def props() = Props(classOf[PlayerManagerActor])
}