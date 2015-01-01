package mmorpg.server

import java.util.UUID

import akka.actor.ActorRef
import mmorpg.messages.ServerMessage._
import mmorpg.messages.Message._
import mmorpg.{Index, Server}
import spray.can.Http.ConnectionClosed
import spray.http.{HttpEntity, MediaTypes}

class ServerWorkerImpl(val serverConnection: ActorRef) extends ServerWorker {

  /*
   * This will need to be changed. There will have to be a better way of associating
   * the connection actor with the player actor
   */
  val id = UUID.randomUUID()

  /**
   * Register new client with the world
   */
  override def onWebSocketOpen(): Unit = {
    Server.world ! ClientConnected(id, self)
  }

  /**
   * Cleanup when a client disconnects
   * @param e The close event
   */
  override def onWebSocketClose(e: ConnectionClosed): Unit = {
    Server.world ! ClientDisconnected(id)
  }

  /**
   * Handle messages from the client
   * @param msg The message from the client
   */
  override def messageHandler(msg: Message): Unit = {
    Server.world ! msg
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