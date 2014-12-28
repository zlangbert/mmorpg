package mmorpg

import akka.actor.{ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.pattern.ask
import akka.util.Timeout
import mmorpg.server.ServerActor
import mmorpg.world.WorldActor
import spray.can.Http
import spray.can.server.UHttp

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Properties

object Server {

  implicit val system = ActorSystem()
  implicit val bindingTimeout: Timeout = 2.second

  val world = system.actorOf(Props[WorldActor], "world")

  /**
   * Starts WebSocket server
   */
  def startWebSocketServer(): Unit = {

    val endpoint = "0.0.0.0"
    val port = Properties.envOrElse("PORT", "8080").toInt

    val serviceActor = system.actorOf(ServerActor.props(), "server")
    IO(UHttp).ask(Http.Bind(serviceActor, endpoint, port = port)).flatMap {
      case b: Http.Bound ⇒ Future.successful(b)
      case Tcp.CommandFailed(b: Http.Bind) =>
        Future.failed(new RuntimeException(
          s"Binding failed on $endpoint:$port."))
    }(system.dispatcher)
  }

  def main(args: Array[String]): Unit = {
    startWebSocketServer()
  }
}