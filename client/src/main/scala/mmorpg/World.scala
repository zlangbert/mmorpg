package mmorpg

import mmorpg.gfx.Renderable
import mmorpg.tmx.{TmxLoader, Tmx}
import org.scalajs.dom.CanvasRenderingContext2D

class World extends Renderable {

  var map: Tmx.Map = null

  override def renderAt(ctx: CanvasRenderingContext2D, x: Int, y: Int): Unit = {

  }

  def init(): Unit = {
    TmxLoader.load("/maps/test/data").onSuccess { case m => map = m }
  }
}