package mmorpg

import java.util.UUID

import scala.util.Random

case class PlayerInfo(id: String, var pos: Point, color: String) {
  def move(dx: Int, dy: Int): Unit = pos = pos + Point(dx, dy)
}

object PlayerInfo {
  def random(): PlayerInfo =
    PlayerInfo(UUID.randomUUID().toString,
      Point(Random.nextInt(800), Random.nextInt(800)),
      s"rgb(${Random.nextInt(256)}, ${Random.nextInt(256)}, ${Random.nextInt(256)})")
}