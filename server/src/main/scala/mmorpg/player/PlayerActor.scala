package mmorpg.player

import java.util.UUID

import akka.actor.{ActorRef, Actor, Props}
import mmorpg.Server
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage.Push
import mmorpg.util.Vec

class PlayerActor(id: UUID, connection: ActorRef) extends Actor {

  val state = PlayerState(id, Vec(0, 0))

  //Initialize
  init()

  override def receive: Receive = {
    case msg: Message => connection ! Push(msg)
  }

  def init(): Unit = {
    connection ! Push(InitializeClient(id))
    Server.world ! Spawn(id, state)
  }
}

object PlayerActor {
  def props(id: UUID, connection: ActorRef) = Props(classOf[PlayerActor], id, connection)
}