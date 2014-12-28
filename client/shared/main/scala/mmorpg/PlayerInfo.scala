package mmorpg

case class PlayerInfo(id: String, var pos: Point, color: String) {
  def move(dx: Int, dy: Int): Unit = pos = pos + Point(dx, dy)
}