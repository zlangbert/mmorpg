package mmorpg

case class Point(x: Int, y: Int) {
  def +(o: Point): Point = Point(x + o.x, y + o.y)
}