package mmorpg.world

import akka.actor.Actor
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.player.PlayerManagerActor

class WorldActor extends Actor {

  val players = context.actorOf(PlayerManagerActor.props(), "players")

  override def receive: Receive = {

    case msg@ClientConnected(_, _) =>
      players ! msg
    case msg@ClientDisconnected(_) =>
      players ! msg

    case msg@Spawn(_, _) =>
      players ! Broadcast(msg)

    /*case MovePlayer(worker, direction) =>
      val player = players(worker)
      direction match {
        case Direction.Up => player.info.move(0, -2)
        case Direction.Down => player.info.move(0, 2)
        case Direction.Left => player.info.move(-2, 0)
        case Direction.Right => player.info.move(2, 0)
      }
      broadcast(Move(player.info))*/
  }
}