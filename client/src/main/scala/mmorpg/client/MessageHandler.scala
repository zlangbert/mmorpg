package mmorpg.client

import mmorpg.messages.Message._

trait MessageHandler {
  def apply(msg: Message): Unit
}

object MessageHandler {
  def apply(): MessageHandler = new MessageHandlerImpl
}

class MessageHandlerImpl extends MessageHandler {
  override def apply(message: Message): Unit = message match {
    case InitializeClient(id) =>
      println("setting id")
      Client.id = id
    case Spawn(id, state) =>
      println(s"spawing ${state.id}")
      Client.players += state.id -> state
    case Despawn(id) =>
      println(s"despawning $id")
      Client.players -= id
    case UpdateState(id, state) =>
      Client.players(id) = state
    case ImageData(_, key, data) =>
      Assets.register(key, data)
    case _ => println(s"Unhandled message: $message")
  }
}