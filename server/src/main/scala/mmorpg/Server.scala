package mmorpg

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.pattern.ask
import akka.util.Timeout
import mmorpg.Messages.Spawn
import spray.can.server.UHttp
import spray.can.{Http, websocket}
import spray.can.websocket.frame.TextFrame
import spray.http.{HttpEntity, MediaTypes}
import spray.routing.HttpServiceActor

import scala.collection.concurrent
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Properties, Random}

object Server {
  
  class HttpService extends HttpServiceActor {
    override def receive: Receive = runRoute {
      get {
        pathSingleSlash {
          complete {
            HttpEntity(MediaTypes.`text/html`, Index.skeleton.render)
          }
        } ~
        getFromResourceDirectory("")
      }
    }
  }

  object WebSocketServer {
    def props() = Props(classOf[WebSocketServer])
  }
  class WebSocketServer extends Actor {
    override def receive: Actor.Receive = {
      // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
      case Http.Connected(remoteAddress, localAddress) =>
        val serverConnection = sender()
        val conn = context.actorOf(WebSocketWorker.props(serverConnection))
        serverConnection ! Http.Register(conn)

        players += conn ->
          Player(conn,
            PlayerInfo(UUID.randomUUID().toString, Point(Random.nextInt(800), Random.nextInt(800)),
            s"rgb(${Random.nextInt(256)}, ${Random.nextInt(256)}, ${Random.nextInt(256)})"))
    }
  }

  /**
   * WebSocket Worker
   */
  object WebSocketWorker {
    def props(serverConnection: ActorRef) = Props(classOf[WebSocketWorker], serverConnection)
  }
  class WebSocketWorker(val serverConnection: ActorRef) extends HttpServiceActor with websocket.WebSocketServerWorker {

    import Messages._

    def broadcast(msg: Messages.Message): Unit = {
      players.values.foreach(_.conn ! Push(msg))
    }

    override def businessLogic: Receive = {
      case websocket.UpgradedToWebSocket =>

        //spawn all players on new client
        players.values.foreach { player =>
          self ! Push(Spawn(player.info))
        }

        //spawn new player on all clients
        val newPlayer = players(self)
        players.values.foreach { player =>
          player.conn ! Push(Spawn(newPlayer.info))
        }

      case Push(msg) => send(TextFrame(upickle.write(msg)))

      case m: TextFrame =>
        val rawMessage = m.payload.decodeString("UTF-8")
        val message = upickle.read[ClientMessage](rawMessage)
        message match {
          case MoveRequest(dir) =>
            val player = players(self)
            dir match {
              case 0 => player.info.move(-2, 0) //left
              case 1 => player.info.move(0, -2) //up
              case 2 => player.info.move(2, 0) //right
              case 3 => player.info.move(0, 2) //down
            }
            broadcast(Move(player.info))
        }


      /*match {
        case "ping" => broadcast(StringMessage("pong"))
        case _ => println("unrecognized message, ignoring")
      }*/
    }

    override def closeLogic: Receive = {
      case ev: Http.ConnectionClosed =>

        val player = players(self)

        players -= self
        broadcast(Despawn(player.info))
        println(players.size)

        context.stop(self)
        log.debug("Connection closed on event: {}", ev)
    }
  }

  val players = concurrent.TrieMap[ActorRef, Player]()

  def startHttpServer(): Unit = {
    val port = Properties.envOrElse("PORT", "8080").toInt

    implicit val system = ActorSystem()
    implicit val bindingTimeout: Timeout = 1.second

    val serviceActor = system.actorOf(Props[HttpService])
    IO(Http).ask(Http.Bind(serviceActor, interface = "0.0.0.0", port = port)).flatMap {
      case b: Http.Bound ⇒ Future.successful(b)
      case Tcp.CommandFailed(b: Http.Bind) =>
        Future.failed(new RuntimeException(
          "Binding failed. Switch on DEBUG-level logging for `akka.io.TcpListener` to log the cause."))
    }(system.dispatcher)
  }

  def startWebSocketServer(): Unit = {

    val port = Properties.envOrElse("PORT", "8080").toInt

    implicit val system = ActorSystem()
    implicit val bindingTimeout: Timeout = 1.second

    val serviceActor = system.actorOf(WebSocketServer.props())
    IO(UHttp).ask(Http.Bind(serviceActor, "0.0.0.0", port = port+1)).flatMap {
      case b: Http.Bound ⇒ Future.successful(b)
      case Tcp.CommandFailed(b: Http.Bind) =>
        Future.failed(new RuntimeException(
          "Binding failed (websocket). Switch on DEBUG-level logging for `akka.io.TcpListener` to log the cause."))
    }(system.dispatcher)
  }

  def main(args: Array[String]): Unit = {

    startHttpServer()
    startWebSocketServer()
  }
}