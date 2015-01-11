package mmorpg.gfx

import mmorpg.Client
import mmorpg.tmx.Tmx
import org.scalajs.dom.extensions.Color

class TileOutliner(map: Tmx.Map) extends Renderable {

  val ValidColor = Color("#FFDF7D").toString()
  val InvalidColor = Color("#DE1028").toString()
  val LineWidth = 3

  override def render()(implicit delta: TimeDelta, ctx: RenderingContext): Unit = {
    val (screenX, screenY) = Client.world.camera.clampToGrid(Client.mouseHandler.x, Client.mouseHandler.y)
    val (worldX, worldY) = Client.world.camera.screenToWorld(screenX, screenY)
    val tileIndex = map.indexFromCoords(worldX, worldY)
    ctx.strokeStyle = if (map.isSolid(tileIndex)) InvalidColor else ValidColor
    ctx.lineWidth = LineWidth
    ctx.strokeRect(screenX, screenY, map.tileSize, map.tileSize)
  }
}