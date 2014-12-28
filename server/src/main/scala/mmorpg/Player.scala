package mmorpg

import akka.actor.ActorRef

case class Player(conn: ActorRef, info: PlayerInfo)