package mmorpg.server

import akka.actor.ActorRef
import mmorpg.messages.ClientMessage._
import mmorpg.world.WorldMessage
import mmorpg.{Server, Index}
import spray.can.Http.ConnectionClosed
import spray.http.{MediaTypes, HttpEntity}

class ServerWorkerImpl(val serverConnection: ActorRef) extends ServerWorker {

  /**
   * Register new client with the world
   */
  override def onWebSocketOpen(): Unit = {
    Server.world ! WorldMessage.ClientConnected(self)
  }

  /**
   * Cleanup when a client disconnects
   * @param e The close event
   */
  override def onWebSocketClose(e: ConnectionClosed): Unit = {
    Server.world ! WorldMessage.ClientDisconnected(self)
  }

  /**
   * Handle messages from the client
   * @param msg The message from the client
   */
  override def clientMessageHandler(msg: ClientMessage): Unit = msg match {
    case MoveRequest(dir) =>
      Server.world ! WorldMessage.MovePlayer(self, dir)
  }

  /*
   * Handle normal http requests
   */
  override def httpHandler: Receive = runRoute {
    get {
      pathSingleSlash {
        complete {
          HttpEntity(MediaTypes.`text/html`, Index.skeleton.render)
        }
      } ~
      getFromResourceDirectory("")
    }
  }
}