package mmorpg.player

import java.util.UUID

import akka.actor._
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._

class PlayerManagerActor(world: ActorRef) extends Actor with ActorLogging {

  /**
   * Actor selections
   */
  val all = context.actorSelection("*")
  def player(id: UUID) = context.actorSelection(s"$id")

  /**
   * Number of connected players
   */
  var playerCount = 0

  override def receive: Receive = {

    /**
     * Forward ticks to players
     */
    case Tick =>
      if (playerCount > 0) all forward Tick

    /**
     * Create a player actor on connect
     */
    case ClientConnected(id, connection) =>
      log.info(s"Player connected: $id")
      playerCount += 1
      context.actorOf(PlayerActor.props(id, connection, world), s"$id")
      player(id) ! Push(InitializeClient(id))

    /**
     * Kill player actor on disconnect
     */
    case ClientDisconnected(id) =>
      log.info(s"Player disconnected: $id")
      playerCount -= 1
      player(id) ! PoisonPill

    /**
     * Forward state update request to all players
     */
    case AnnounceSpawn =>
      all forward AnnounceSpawn

    /**
     * Send message to all players
     */
    case Broadcast(msg) =>
      all ! msg

    /**
     * Send message to all players and push it
     */
    case BroadcastPush(msg) =>
      all ! Push(msg)

    /**
     * Forward any other message to the matching player
     */
    case msg: Message =>
      val id = msg.target
      player(id) forward msg
  }
}

object PlayerManagerActor {
  def props(world: ActorRef) = Props(classOf[PlayerManagerActor], world)
}