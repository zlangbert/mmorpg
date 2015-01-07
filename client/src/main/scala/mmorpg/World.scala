package mmorpg

import mmorpg.assets._
import mmorpg.gfx.sprites.Sprite
import mmorpg.gfx.tiles.Tileset
import mmorpg.gfx.{Renderable, RenderingContext, TimeDelta, TmxRenderer}
import mmorpg.tmx.Tmx
import mmorpg.util.{DelayedInit, Logging}

import scala.async.Async._
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class World extends Renderable with DelayedInit with Logging {

  //these should all be constructor args and the world built with a static world loader/builder
  private var map: Tmx.Map = null
  private var renderer: TmxRenderer = null
  private var sprites: Seq[Sprite] = null
  private var playerSprite: Sprite = null

  waitFor(init())

  def getTileIndex(x: Int, y: Int): Int = {
    if (x > map.width * 48 || y > map.height * 48) -1
    else y / 48 * map.height + x / 48
  }

  def tileIsWalkable(tileIndex: Int): Boolean = {
    !map.isSolid(tileIndex)
  }

  override def renderAt(x: Int, y: Int)(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    renderer.renderAt(0, 0)

    ctx.strokeStyle = if (tileIsWalkable(getTileIndex(Client.mouseHandler.x, Client.mouseHandler.y))) "#FFDF7D" else "#DE1028"

    ctx.lineWidth = 3
    ctx.strokeRect(Client.mouseHandler.x / 48 * 48, Client.mouseHandler.y / 48 * 48, 48, 48)

    Client.players.values.foreach { player =>
      playerSprite.renderAt(player.position.x, player.position.y)
    }
  }

  private def init(): Future[Unit] = async {
    val mapKey = "test"
    map = await(MapLoader(mapKey))
    val tilesets = await(Future.sequence(map.tilesets.map(Tileset.apply(mapKey))))
    renderer = new TmxRenderer(map, tilesets)

    val spriteKeys = await(SpriteLoader.spriteKeys)
    sprites = await(Future.sequence(spriteKeys.map(SpriteLoader.apply)))
    playerSprite = sprites.find(_.key == "clotharmor").get
  }
}