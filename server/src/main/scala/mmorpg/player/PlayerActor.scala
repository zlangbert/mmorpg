package mmorpg.player

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import mmorpg.Server
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.util.Vec

class PlayerActor(id: UUID, connection: ActorRef, world: ActorRef) extends Actor {

  val state = PlayerState(id, Vec(0, 0))

  override def receive: Receive = {

    case Tick =>

    case Move(_, direction) =>
      state.move(direction)
      world ! UpdateState(id, state)

    case msg: Push => connection ! msg
  }

  /*
   * Initialize
   */
  init()

  def init(): Unit = {
    connection ! Push(InitializeClient(id))
    Server.world ! Spawn(id, state)
  }
}

object PlayerActor {
  def props(id: UUID, connection: ActorRef, world: ActorRef) = Props(classOf[PlayerActor], id, connection, world)
}