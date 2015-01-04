package mmorpg

import mmorpg.messages.Message._
import mmorpg.util.Logging

trait MessageHandler {
  def apply(msg: Message): Unit
}

object MessageHandler {
  def apply(): MessageHandler = new MessageHandlerImpl
}

class MessageHandlerImpl extends MessageHandler with Logging {
  override def apply(message: Message): Unit = message match {
    case InitializeClient(id) =>
      Log.debug(s"setting id: $id")
      Client.id = id
    case Spawn(_, state) =>
      Log.debug(s"spawing ${state.id}")
      Client.players += state.id -> state
    case Despawn(id) =>
      Log.debug(s"despawning $id")
      Client.players -= id
    case UpdateState(id, state) =>
      Client.players(id) = state
  }
}