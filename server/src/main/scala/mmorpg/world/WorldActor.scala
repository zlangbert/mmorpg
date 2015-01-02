package mmorpg.world

import akka.actor.Actor
import akka.util.Timeout
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.player.{PlayerState, PlayerManagerActor}
import scala.concurrent.duration._
import akka.pattern.ask

class WorldActor extends Actor {

  val players = context.actorOf(PlayerManagerActor.props(self), "players")

  /*
   * Schedule world ticks
   */
  context.system.scheduler.schedule(1.second, 50.milliseconds, self, Tick)(context.dispatcher, self)

  override def receive: Receive = {

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

    case Tick =>
      players ! Tick
  }
}