package mmorpg

import java.util.UUID

import mmorpg.messages.Message._
import mmorpg.net.WebSocketConnection
import mmorpg.player.PlayerState
import mmorpg.util.Direction
import org.scalajs.dom
import org.scalajs.dom._

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExport

@JSExport
object Client {

  var id: UUID = null

  var leftPressed = false
  var upPressed = false
  var rightPressed = false
  var downPressed = false

  val players = mutable.Map[UUID, PlayerState]()

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

      if (leftPressed) socket.send(Move(id, Direction.Left))
      if (upPressed) socket.send(Move(id, Direction.Up))
      if (rightPressed) socket.send(Move(id, Direction.Right))
      if (downPressed) socket.send(Move(id, Direction.Down))

      clear(ctx)

      players.values.foreach { player =>
        ctx.fillStyle = "red"//player.color
        ctx.fillRect(player.position.x, player.position.y, 25, 25)
      }

      dom.window.requestAnimationFrame(step _)
    }
    dom.window.requestAnimationFrame(step _)
  }
}