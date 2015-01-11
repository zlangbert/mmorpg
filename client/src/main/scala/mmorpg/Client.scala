package mmorpg

import java.util.UUID

import mmorpg.gfx._
import mmorpg.input.MouseHandler
import mmorpg.messages.Message.Move
import mmorpg.net.WebSocketConnection
import mmorpg.player.PlayerState
import mmorpg.util.Vec
import org.scalajs.dom
import org.scalajs.dom.extensions.Color
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, MouseEvent, UIEvent}

import scala.collection.mutable
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.JSExport
import scala.async.Async._

@JSExport
object Client {

  var id: UUID = null

  val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
  implicit val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  val mouseHandler = MouseHandler(canvas)

  val players = mutable.Map[UUID, PlayerState]()

  var _socket: WebSocketConnection = null
  private def socket = _socket

  var _world: World = null
  def world: World = _world

  @JSExport
  def main(container: dom.HTMLDivElement) = {

    DebugInfo.attach(container)

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    dom.window.onresize = { e: UIEvent =>
      canvas.width = canvas.parentElement.clientWidth
      canvas.height = canvas.parentElement.clientHeight
    }

    mouseHandler.onClick { e: MouseEvent =>
      val screenX = e.clientX.toInt
      val screenY = e.clientY.toInt
      val (worldX, worldY) = world.camera.screenToWorld(screenX, screenY)
      socket.send(Move(id, worldX, worldY))
    }

    async {
      val socket = await(WebSocketConnection(dom.window.location.hostname, 8080, MessageHandler()))
      _socket = socket
      val world = await(World("test"))
      _world = world
      update(socket, world)
    }
  }

  def update(socket: WebSocketConnection, world: World)(implicit ctx: CanvasRenderingContext2D): Unit = {

    var lastTime: Double = 0

    def step(time: Double): Unit = {

      implicit val delta: TimeDelta = time - lastTime
      lastTime = time

      DebugInfo.frameStart()

      ctx.clear(Color.Black)

      world.render()

      ctx.fillStyle = "white"
      ctx.font = "14px Arial"
      ctx.fillText("Camera", canvas.width - 100, 125)
      ctx.fillText(world.camera.position.toString, canvas.width - 100, 140)
      ctx.fillText("Mouse", canvas.width - 100, 160)
      ctx.fillText(s"${world.camera.screenToWorld(mouseHandler.x, mouseHandler.y)}", canvas.width - 100, 175)

      DebugInfo.frameEnd()

      dom.window.requestAnimationFrame(step _)
    }
    dom.window.requestAnimationFrame(step _)
  }
}
