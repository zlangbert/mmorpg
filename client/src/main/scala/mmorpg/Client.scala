package mmorpg

import mmorpg.messages.ClientMessage._
import mmorpg.messages.ServerMessage._
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

    val socket = new dom.WebSocket(s"ws://${dom.window.location.hostname}:8080")
    socket.onopen = { e: Event =>
      //socket.send("ping")
      //socket.send("idk")
    }
    socket.onmessage = { e: MessageEvent =>
      val message = upickle.read[ServerMessage](e.data.toString)
      message match {
        case StringMessage(msg) => println(msg)
        case Spawn(player) =>
          println(s"spawing $player")
          players += player
        case Despawn(player) =>
          println(s"despawing $player")
          players.find(_.id == player.id).foreach(players -= _)
        case Move(player) =>
          players.find(_.id == player.id).map(_.pos = player.pos)
      }
    }

    val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    dom.document.onkeydown = { e: KeyboardEvent =>
      e.keyCode match {
        case 37 => leftPressed = true
        case 38 => upPressed = true
        case 39 => rightPressed = true
        case 40 => downPressed = true
      }
    }

    dom.document.onkeyup = { e: KeyboardEvent =>
      e.keyCode match {
        case 37 => leftPressed = false
        case 38 => upPressed = false
        case 39 => rightPressed = false
        case 40 => downPressed = false
      }
    }

    clear(ctx)

    update(socket, ctx)
  }

  def clear(ctx: CanvasRenderingContext2D): Unit = {
    ctx.fillStyle = "#ffffff"
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
  }

  def update(socket: dom.WebSocket, ctx: CanvasRenderingContext2D): Unit = {

    def step(time: Double): Unit = {

      if (leftPressed) socket.send(upickle.write(MoveRequest(0)))
      if (upPressed) socket.send(upickle.write(MoveRequest(1)))
      if (rightPressed) socket.send(upickle.write(MoveRequest(2)))
      if (downPressed) socket.send(upickle.write(MoveRequest(3)))

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