package mmorpg.server

import akka.actor.{ActorRef, Props}
import mmorpg.messages.ClientMessage.ClientMessage
import mmorpg.messages.ServerMessage
import spray.can.websocket.frame.TextFrame
import spray.can.{Http, websocket}
import spray.routing.HttpServiceActor

trait ServerWorker extends HttpServiceActor with websocket.WebSocketServerWorker {

  override def receive = handshaking orElse httpHandler orElse closeLogic

  def httpHandler: Receive
  def onWebSocketOpen(): Unit
  def onWebSocketClose(e: Http.ConnectionClosed): Unit
  def clientMessageHandler(msg: ClientMessage): Unit

  override def businessLogic: Receive = {

    /**
     * New connection open
     */
    case websocket.UpgradedToWebSocket => onWebSocketOpen()

    /**
     * Push a message to the client associated with this worker
     */
    case ServerMessage.Push(msg) => send(TextFrame(upickle.write(msg)))

    /**
     * Received data
     */
    case m: TextFrame =>
      val rawMessage = m.payload.decodeString("UTF-8")
      val message = upickle.read[ClientMessage](rawMessage)
      clientMessageHandler(message)
  }

  override def closeLogic: Receive = {
    case ev: Http.ConnectionClosed =>
      onWebSocketClose(ev)
      super.closeLogic(ev)
  }
}

object ServerWorker {
  def props(serverConnection: ActorRef) = Props(classOf[ServerWorkerImpl], serverConnection)
}