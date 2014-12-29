package mmorpg.world

import akka.actor.{Actor, ActorRef}
import mmorpg.messages.ServerMessage._
import mmorpg.util.Direction
import mmorpg.world.WorldMessage.{ClientConnected, ClientDisconnected, MovePlayer}
import mmorpg.{Player, PlayerInfo}

import scala.collection.concurrent

class WorldActor extends Actor {

  private val players = concurrent.TrieMap[ActorRef, Player]()

  override def receive: Receive = {

    case ClientConnected(worker) =>
      val info = PlayerInfo.random()
      players += worker -> Player(worker, info)
      broadcast(Spawn(info))

    case ClientDisconnected(worker) =>
      val player = players(worker)
      players -= worker
      broadcast(Despawn(player.info))

    case MovePlayer(worker, direction) =>
      val player = players(worker)
      direction match {
        case Direction.Up => player.info.move(0, -2)
        case Direction.Down => player.info.move(0, 2)
        case Direction.Left => player.info.move(-2, 0)
        case Direction.Right => player.info.move(2, 0)
      }
      broadcast(Move(player.info))
  }

  private def broadcast(msg: ServerMessage): Unit = {
    players.values.foreach(_.worker ! Push(msg))
  }
}