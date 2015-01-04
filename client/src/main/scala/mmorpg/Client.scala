package mmorpg

import java.util.UUID

import mmorpg.gfx._
import mmorpg.input.MouseHandler
import mmorpg.messages.Message.Move
import mmorpg.net.WebSocketConnection
import mmorpg.player.PlayerState
import mmorpg.util.DelayedInit
import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, MouseEvent}

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object Client {

  var id: UUID = null

  val socket = WebSocketConnection(dom.window.location.hostname, 8080, MessageHandler())

  val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
  implicit val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  val mouseHandler = MouseHandler(canvas)

  val world = new World
  val players = mutable.Map[UUID, PlayerState]()

  @JSExport
  def main(container: dom.HTMLDivElement) = {

    DebugInfo.attach(container)

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    canvas.onclick = { e: MouseEvent =>
      val tileIndex = (e.clientY / 48).toInt * 40 + (e.clientX / 48).toInt
      socket.send(Move(id, tileIndex))
    }

    DelayedInit.waitFor(Assets, socket, world) {
      update(socket)
    }
  }

  def update(socket: WebSocketConnection)(implicit ctx: CanvasRenderingContext2D): Unit = {

    def step(time: Double): Unit = {

      DebugInfo.frameStart()

      ctx.clear()

      world.renderAt(0, 0)

      ctx.strokeStyle = "#FFDF7D"
      ctx.lineWidth = 3
      ctx.strokeRect(mouseHandler.x / 48 * 48, mouseHandler.y / 48 * 48, 48, 48)

      players.values.foreach { player =>
        ctx.beginPath()
        ctx.arc(player.position.x, player.position.y, 15, 0, 2 * js.Math.PI, false)
        ctx.fillStyle = player.color
        ctx.fill()
      }

      DebugInfo.frameEnd()

      dom.window.requestAnimationFrame(step _)
    }
    dom.window.requestAnimationFrame(step _)
  }
}
