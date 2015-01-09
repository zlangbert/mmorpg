package mmorpg

import mmorpg.assets._
import mmorpg.gfx.tiles.Tileset
import mmorpg.gfx._
import mmorpg.tmx.Tmx
import mmorpg.util.{Vec, Logging}

import scala.async.Async._
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class World(map: Tmx.Map, renderer: TmxRenderer) extends Renderable with Logging {

  val camera = new Camera(Vec(32, 32))

  def getTileIndex(x: Int, y: Int): Int = {
    if (x > map.width * 48 || y > map.height * 48) -1
    else y / 48 * map.height + x / 48
  }

  def tileIsWalkable(tileIndex: Int): Boolean = {
    !map.isSolid(tileIndex)
  }

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    renderer.render(camera)

    ctx.strokeStyle = if (tileIsWalkable(getTileIndex(Client.mouseHandler.x, Client.mouseHandler.y))) "#FFDF7D" else "#DE1028"

    ctx.lineWidth = 3
    ctx.strokeRect(Client.mouseHandler.x / 48 * 48, Client.mouseHandler.y / 48 * 48, 48, 48)

    Client.players.values.foreach { player =>
      //playerSprite.renderAt(player.position.x, player.position.y)
    }
  }
}

object World {

  /**
   * Loads required assets and builds a new world
   * @param mapKey The key of the map to load
   * @return A new world
   */
  def apply(mapKey: String): Future[World] = {
    async {
      val map = await(MapLoader(mapKey))
      val tilesets = await(Future.sequence(map.tilesets.map(Tileset.apply(mapKey))))
      val renderer = new TmxRenderer(map, tilesets)
      new World(map, renderer)
    }
  }
}