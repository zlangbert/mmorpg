package mmorpg.client.net

object ConnectionState {

  sealed trait ConnectionState
  case object Connecting extends ConnectionState
  case object Open extends ConnectionState
  case object Closing extends ConnectionState
  case object Closed extends ConnectionState

  def fromReadyState(s: Int): ConnectionState = s match {
    case 0 => Connecting
    case 1 => Open
    case 2 => Closing
    case 3 => Closed
  }
}