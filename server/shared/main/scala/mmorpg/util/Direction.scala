package mmorpg.util

object Direction extends Enumeration {

  sealed trait Direction
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction

  def fromKeyCode(code: Int): Direction = {
    code match {
      case 37 => Left
      case 38 => Up
      case 39 => Right
      case 40 => Down
    }
  }
}