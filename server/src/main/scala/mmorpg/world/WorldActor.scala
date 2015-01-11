package mmorpg.world

import akka.actor.Actor
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.player.PlayerManagerActor
import mmorpg.tmx.TmxLoader

import scala.concurrent.Await
import scala.concurrent.duration._

class WorldActor extends Actor {

  val map = Await.result(TmxLoader.load("test"), 3.seconds)

  val players = context.actorOf(PlayerManagerActor.props(self), "players")

  /*
   * Schedule world ticks
   */
  context.system.scheduler.schedule(1.second, 50.milliseconds, self, Tick)(context.dispatcher, self)

  override def receive: Receive = {

    case Tick =>
      players ! Tick

    case msg: ClientConnected =>
      players ! msg
      // Spawn all already connected players
      players ! AnnounceSpawn

    case msg: ClientDisconnected =>
      players ! msg
      players ! BroadcastPush(Despawn(msg.id))

    case msg: UpdateState => players ! BroadcastPush(msg)
    case msg: Spawn => players ! BroadcastPush(msg)
    case msg: Move => players ! msg

    case MoveRequest(id, x, y) =>
      sender() ! true//(tileIndex > 0 && !map.isSolid(tileIndex))
  }
}