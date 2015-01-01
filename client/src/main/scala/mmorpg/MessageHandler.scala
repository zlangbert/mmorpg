package mmorpg

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
      println(s"spawing $id")
      Client.players += id -> state
    //players += player
    /*case Despawn(player) =>
      println(s"despawing $player")
    //players.find(_.id == player.id).foreach(players -= _)
    case Move(player) =>*/
    //players.find(_.id == player.id).map(_.pos = player.pos)
  }
}