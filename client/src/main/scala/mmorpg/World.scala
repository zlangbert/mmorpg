package mmorpg

import mmorpg.assets._
import mmorpg.gfx._
import mmorpg.gfx.sprites.Sprite
import mmorpg.gfx.tiles.Tileset
import mmorpg.tmx.Tmx
import mmorpg.util.{Logging, Vec}

import scala.async.Async._
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class World(map: Tmx.Map, renderer: TmxRenderer, playerSprite: Sprite) extends Renderable with Logging {

  val camera = new Camera(Vec(map.width * 48, map.height * 48))

  private val tileOutliner = new TileOutliner(map)

  def getTileIndex(x: Int, y: Int): Int = {
    if (x > map.width * 48 || y > map.height * 48) -1
    else y / 48 * map.height + x / 48
  }

  def tileIsWalkable(tileIndex: Int): Boolean = {
    !map.isSolid(tileIndex)
  }

  override def render()(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    renderer.render(camera)

    tileOutliner.render()

    Client.players.values.foreach { player =>
      playerSprite.renderAt(player.position.x, player.position.y)
    }
  }
}

object World {

  /**
   * Loads required assets and builds a new world
   * @param mapKey The key of the map to load
   * @return A new world
   */
  def apply(mapKey: String): Future[World] = async {
    val map = await(MapLoader(mapKey))
    val tilesets = await(Future.sequence(map.tilesets.map(Tileset.apply(mapKey))))
    val renderer = new TmxRenderer(map, tilesets)
    val playerSprite = await(SpriteLoader("clotharmor"))
    new World(map, renderer, playerSprite)
  }
}