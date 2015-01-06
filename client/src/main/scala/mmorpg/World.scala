package mmorpg

import mmorpg.assets.Assets
import mmorpg.gfx.{Renderable, RenderingContext, TmxRenderer}
import mmorpg.tmx.{Tmx, TmxLoader}
import mmorpg.util.{DelayedInit, Logging}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class World extends Renderable with DelayedInit with Logging {

  private var map: Tmx.Map = null
  private var renderer: TmxRenderer = null

  init()

  override def renderAt(x: Int, y: Int)(implicit ctx: RenderingContext): Unit = {
    renderer.renderAt(0, 0)
  }

  private def loadMap(key: String): Future[Tmx.Map] = {
    Log.debug(s"loading map: $key")
    TmxLoader.load(key)
  }

  private def init(): Unit = {
    waitFor(loadMap("test")).onSuccess { case m =>
      map = m
      Assets.loadTilesets("test", map)
      renderer = new TmxRenderer(map)
    }
  }
}