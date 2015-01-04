package mmorpg.net

import mmorpg.MessageHandler
import mmorpg.messages.Message._
import mmorpg.net.ConnectionState.ConnectionState
import mmorpg.util.DelayedInit
import org.scalajs.dom
import org.scalajs.dom.MessageEvent

import scala.concurrent.Promise

class WebSocketConnection(url: String, port: Int, messageHandler: MessageHandler) extends DelayedInit {

  private val socket = new dom.WebSocket(s"ws://$url:$port")

  socket.onmessage = { e: MessageEvent =>
    val message = upickle.read[Message](e.data.toString)
    messageHandler(message)
  }

  //wait for the socket to open before being ready
  waitFor({
    val p = Promise[Unit]()
    socket.onopen = { e: dom.Event =>
      p.success()
    }
    p.future
  })

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
  def apply(url: String, port: Int, messageHandler: MessageHandler) =
    new WebSocketConnection(url, port, messageHandler)
}