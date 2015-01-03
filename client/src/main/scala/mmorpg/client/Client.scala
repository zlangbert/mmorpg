package mmorpg.client

import java.util.UUID

import mmorpg.client.gfx.SpriteSheet
import mmorpg.client.net.WebSocketConnection
import mmorpg.messages.Message._
import mmorpg.player.PlayerState
import org.scalajs.dom
import org.scalajs.dom._

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object Client {

  var id: UUID = null

  var mouseX = 0
  var mouseY = 0

  val players = mutable.Map[UUID, PlayerState]()
  val world = Array.tabulate(40, 40) { case (x, y) =>
    val sheet = SpriteSheet(Assets("tilesheet"))
    sheet(172)
  }

  @JSExport
  def main(container: dom.HTMLDivElement) = {

    DebugInfo.attach(container)

    val socket = WebSocketConnection(dom.window.location.hostname, 8080, MessageHandler())

    val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    canvas.onmousemove = { e: MouseEvent =>
      mouseX = e.clientX.toInt
      mouseY = e.clientY.toInt
    }

    canvas.onclick = { e: MouseEvent =>
      val tileIndex = (e.clientY / 48).toInt * 40 + (e.clientX / 48).toInt
      socket.send(Move(id, tileIndex))
    }

    Assets.onReady { () =>
      update(socket, ctx)
    }
  }

  def clear(ctx: CanvasRenderingContext2D): Unit = {
    ctx.fillStyle = "#ffffff"
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height)
  }

  def update(socket: WebSocketConnection, ctx: CanvasRenderingContext2D): Unit = {

    def step(time: Double): Unit = {

      DebugInfo.frameStart()

      clear(ctx)

      for (x <- 0 until world.size; y <- 0 until world.size) {
        val sprite = world(x)(y)
        sprite.renderAt(ctx, x*48, y*48)
      }

      ctx.strokeStyle = "#FFDF7D"
      ctx.lineWidth = 3
      ctx.strokeRect(mouseX / 48 * 48, mouseY / 48 * 48, 48, 48)

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