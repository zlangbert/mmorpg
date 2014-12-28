package mmorpg.server

import akka.actor.{Actor, Props}
import spray.can.Http

/**
 * Entry point for clients. Creates a worker per connection
 */
class ServerActor extends Actor {
  override def receive: Actor.Receive = {
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()
      val worker = context.actorOf(ServerWorker.props(serverConnection))
      serverConnection ! Http.Register(worker)
  }
}

object ServerActor {
  def props() = Props(classOf[ServerActor])
}