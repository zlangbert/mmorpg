package mmorpg

import mmorpg.messages.ServerMessage._

trait MessageHandler {
  def apply(msg: ServerMessage): Unit
}

object MessageHandler {
  def apply(): MessageHandler = new MessageHandlerImpl
}

class MessageHandlerImpl extends MessageHandler {
  override def apply(message: ServerMessage): Unit = message match {
    case StringMessage(msg) => println(msg)
    case Spawn(player) =>
      println(s"spawing $player")
    //players += player
    case Despawn(player) =>
      println(s"despawing $player")
    //players.find(_.id == player.id).foreach(players -= _)
    case Move(player) =>
    //players.find(_.id == player.id).map(_.pos = player.pos)
  }
}