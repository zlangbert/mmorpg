package mmorpg.player

import java.util.UUID

import mmorpg.util.Vec

import scala.util.Random

case class PlayerState(id: UUID, var position: Vec,
                       color: String = s"rgb(${Random.nextInt(256)}, ${Random.nextInt(256)}, ${Random.nextInt(256)})") {

  def move(tileIndex: Int): Unit = {
    val tilesX = 40
    val x = tileIndex % tilesX * 48 + 48 / 2
    val y = tileIndex / tilesX * 48 + 48 / 2
    position = Vec(x, y)
  }
}

object PlayerState {
  //waiting for upickle fix
  //def apply(id: UUID): PlayerState = PlayerState(id, Vec())
}