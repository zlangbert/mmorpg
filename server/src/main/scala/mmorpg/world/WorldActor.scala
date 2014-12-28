package mmorpg.world

import akka.actor.{Actor, ActorRef}
import mmorpg.messages.ServerMessage._
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
        case 0 => player.info.move(-2, 0) //left
        case 1 => player.info.move(0, -2) //up
        case 2 => player.info.move(2, 0) //right
        case 3 => player.info.move(0, 2) //down
      }
      broadcast(Move(player.info))
  }

  private def broadcast(msg: ServerMessage): Unit = {
    players.values.foreach(_.worker ! Push(msg))
  }
}