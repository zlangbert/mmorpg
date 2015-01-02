package mmorpg.player

import java.util.UUID

import akka.actor.{ActorRef, PoisonPill, Props, Actor}
import akka.event.Logging
import mmorpg.messages.Message.Message
import mmorpg.messages.ServerMessage._

class PlayerManagerActor(world: ActorRef) extends Actor {

  /*
   * Actor selections
   */
  val all = context.actorSelection("*")
  def player(id: UUID) = context.actorSelection(s"$id")

  /*
   * Number of connected players
   */
  var playerCount = 0

  override def receive: Receive = {

    /*
     * Create a player actor on connect
     */
    case ClientConnected(id, connection) =>
      println(s"Player connected: $id")
      playerCount += 1
      context.actorOf(PlayerActor.props(id, connection, world), s"$id")

    /*
     * Kill player actor on disconnect
     */
    case ClientDisconnected(id) =>
      playerCount -= 1
      player(id) ! PoisonPill

    /*
     * Forward ticks to players
     */
    case Tick =>
      if (playerCount > 0) all ! Tick

    /*
     * Send message to all players
     */
    case Broadcast(msg) =>
      all ! msg

    /*
     * Send message to all players and push it
     */
    case BroadcastPush(msg) =>
      all ! Push(msg)

    /*
     * Forward any other message to the matching player
     */
    case msg: Message =>
      val id = msg.id
      player(id) ! msg
  }
}

object PlayerManagerActor {
  def props(world: ActorRef) = Props(classOf[PlayerManagerActor], world)
}