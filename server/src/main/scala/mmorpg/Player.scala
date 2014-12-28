package mmorpg

import akka.actor.ActorRef

case class Player(worker: ActorRef, info: PlayerInfo)