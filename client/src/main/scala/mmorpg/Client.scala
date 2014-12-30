package mmorpg

import mmorpg.messages.ClientMessage._
import mmorpg.net.WebSocketConnection
import mmorpg.util.Direction
import org.scalajs.dom
import org.scalajs.dom._

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExport

@JSExport
object Client {

  var leftPressed = false
  var upPressed = false
  var rightPressed = false
  var downPressed = false

  val players = mutable.Set[PlayerInfo]()

  @JSExport
  def main(container: dom.HTMLDivElement) = {

    val socket = WebSocketConnection(dom.window.location.hostname, 8080, MessageHandler())

    val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    dom.document.onkeydown = { e: KeyboardEvent =>
      Direction.fromKeyCode(e.keyCode) match {
        case Direction.Up => upPressed = true
        case Direction.Down => downPressed = true
        case Direction.Left => leftPressed = true
        case Direction.Right => rightPressed = true
      }
    }

    dom.document.onkeyup = { e: KeyboardEvent =>
      Direction.fromKeyCode(e.keyCode) match {
        case Direction.Up => upPressed = false
        case Direction.Down => downPressed = false
        case Direction.Left => leftPressed = false
        case Direction.Right => rightPressed = false
      }
    }

    clear(ctx)

    update(socket, ctx)
  }

  def clear(ctx: CanvasRenderingContext2D): Unit = {
    ctx.fillStyle = "#ffffff"
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
  }

  def update(socket: WebSocketConnection, ctx: CanvasRenderingContext2D): Unit = {

    def step(time: Double): Unit = {

      if (leftPressed) socket.send(MoveRequest(Direction.Left))
      if (upPressed) socket.send(MoveRequest(Direction.Up))
      if (rightPressed) socket.send(MoveRequest(Direction.Right))
      if (downPressed) socket.send(MoveRequest(Direction.Down))

      clear(ctx)

      players.foreach { player =>
        ctx.fillStyle = player.color
        ctx.fillRect(player.pos.x, player.pos.y, 25, 25)
      }

      dom.window.requestAnimationFrame(step _)
    }
    dom.window.requestAnimationFrame(step _)
  }
}