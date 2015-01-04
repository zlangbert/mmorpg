package mmorpg

import mmorpg.gfx.{Renderable, TmxRenderer}
import mmorpg.tmx.{Tmx, TmxLoader}
import mmorpg.util.DelayedInit
import org.scalajs.dom.CanvasRenderingContext2D

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class World extends Renderable with DelayedInit {

  private var map: Tmx.Map = null
  private var renderer: TmxRenderer = null

  init()

  override def renderAt(x: Int, y: Int)(implicit ctx: CanvasRenderingContext2D): Unit = {
      renderer.renderAt(0, 0)
  }

  private def init(): Unit = {
    //load map
    waitFor(TmxLoader.load("test")).onSuccess { case m =>
      map = m
      renderer = new TmxRenderer(map)
    }
  }
}