package mmorpg.gfx

import mmorpg.Client
import mmorpg.util.Vec

class Camera(val size: Vec) {

  type Position = (Int, Int)

  var _position: Position = Client.canvas.width / 2 -> Client.canvas.height / 2

  def position = _position
  def position_=(p: Position) = _position = p

  def worldToScreen(x: Int, y: Int): (Int, Int) = {
    val (px, py) = position
    val screenX = x - (px - Client.canvas.width / 2)
    val screenY = y - (py - Client.canvas.height / 2)
    screenX -> screenY
  }

  def screenToWorld(x: Int, y: Int): (Int, Int) = {
    val (px, py) = position
    val worldX = x + (px - Client.canvas.width / 2)
    val worldY = y + (py - Client.canvas.height / 2)
    worldX -> worldY
  }

  /**
   * Clamp coordinates to the top left of the tile
   * @param x
   * @param y
   * @return
   */
  def clampToGrid(x: Int, y: Int): (Int, Int) = {
    val clampedX = math.floor(x / 48).toInt * 48
    val clampedY = math.floor(y / 48).toInt * 48
    clampedX -> clampedY
  }
}