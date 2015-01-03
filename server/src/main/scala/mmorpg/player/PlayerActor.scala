package mmorpg.player

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import mmorpg.Server
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.util.Vec

/**
 * A per player actor in charge of tracking and mutating state
 * @param id Id of the client
 * @param connection The websocket connection actor
 * @param world The game world
 */
class PlayerActor(id: UUID, connection: ActorRef, world: ActorRef /*TODO: is this needed?*/) extends Actor {

  val state = PlayerState(id, Vec(0, 0))

  override def receive: Receive = messageHandler orElse toClient

  /**
   * Handles server messages
   */
  lazy val messageHandler: Receive = {
    case Tick =>
    case AnnounceSpawn => sender() ! Spawn(id, state)
    case Move(_, tileIndex) =>
      state.move(tileIndex)
      sender() ! UpdateState(id, state)
  }

  /**
   * Handles a message intended for the client
   */
  lazy val toClient: Receive = {
    case msg: Push => connection ! msg
  }

  /*
   Trigger player spawn
   */
  Server.world ! Spawn(id, state)
}

object PlayerActor {
  def props(id: UUID, connection: ActorRef, world: ActorRef) = Props(classOf[PlayerActor], id, connection, world)
}