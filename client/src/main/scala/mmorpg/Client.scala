package mmorpg

import java.util.UUID

import mmorpg.gfx._
import mmorpg.input.MouseHandler
import mmorpg.messages.Message.Move
import mmorpg.net.WebSocketConnection
import mmorpg.player.PlayerState
import org.scalajs.dom
import org.scalajs.dom.extensions.Color
import org.scalajs.dom.{CanvasRenderingContext2D, HTMLCanvasElement, MouseEvent, UIEvent}

import scala.collection.mutable
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.JSExport

@JSExport
object Client {

  var id: UUID = null

  val socket = WebSocketConnection(dom.window.location.hostname, 8080, MessageHandler())

  val canvas = dom.document.getElementById("canvas").asInstanceOf[HTMLCanvasElement]
  implicit val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

  val mouseHandler = MouseHandler(canvas)

  val players = mutable.Map[UUID, PlayerState]()

  var _world: Option[World] = None
  def world: World = _world.getOrElse(throw new Exception("world not ready"))

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
      val tileIndex = world.getTileIndex(e.clientX.toInt, e.clientY.toInt)
      socket.send(Move(id, tileIndex))
    }

    /*DelayedInit.waitFor(socket, world) {
      update(socket)
    }*/

    World("test").onSuccess {
      case world =>
        _world = Option(world)
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

      world.renderAt(0, 0)

      DebugInfo.frameEnd()

      dom.window.requestAnimationFrame(step _)
    }
    dom.window.requestAnimationFrame(step _)
  }
}
