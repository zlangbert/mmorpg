package mmorpg.client.net

import mmorpg.client.MessageHandler
import mmorpg.messages.Message._
import mmorpg.client.net.ConnectionState.ConnectionState
import org.scalajs.dom
import org.scalajs.dom.MessageEvent

class WebSocketConnection(url: String, port: Int, messageHandler: MessageHandler) {

  private val socket = new dom.WebSocket(s"ws://$url:$port")

  socket.onmessage = { e: MessageEvent =>
    val message = upickle.read[Message](e.data.toString)
    messageHandler(message)
  }

  /**
   * Gets the state of the underlying socket
   * @return The state of the connection
   */
  def state: ConnectionState = ConnectionState.fromReadyState(socket.readyState)

  /**
   * Sends a message to the server
   * @param msg The message to send
   */
  def send(msg: Message): Unit = state match {
    case ConnectionState.Open => socket.send(upickle.write(msg))
    case _ => //just ignore for now
  }
}

object WebSocketConnection {

  def apply(url: String, port: Int, messageHandler: MessageHandler): WebSocketConnection =
    new WebSocketConnection(url, port, messageHandler)
}