package mmorpg.server

import java.nio.file.{Path, Files, Paths}
import java.util.UUID

import akka.actor.ActorRef
import mmorpg.messages.Message._
import mmorpg.messages.ServerMessage._
import mmorpg.{Index, Server}
import spray.can.Http.ConnectionClosed
import spray.http.{HttpHeaders, HttpHeader, HttpEntity, MediaTypes}

class ServerWorkerImpl(val serverConnection: ActorRef) extends ServerWorker {

  /*
   * This will need to be changed. There will have to be a better way of associating
   * the connection actor with the player actor
   */
  val id = UUID.randomUUID()

  /**
   * Register new client with the world
   */
  override def onWebSocketOpen(): Unit = {
    Server.world ! ClientConnected(id, self)
  }

  /**
   * Cleanup when a client disconnects
   * @param e The close event
   */
  override def onWebSocketClose(e: ConnectionClosed): Unit = {
    Server.world ! ClientDisconnected(id)
  }

  /**
   * Handle messages from the client
   * @param msg The message from the client
   */
  override def messageHandler(msg: Message): Unit = {
    Server.world ! msg
  }

  /*
   * Handle normal http requests
   */
  override def httpHandler: Receive = runRoute {
    get {
      pathSingleSlash {
        complete {
          HttpEntity(MediaTypes.`text/html`, Index.skeleton.render)
        }
      } ~
      //TODO: use autowire
      path("maps" / Segment / "data") { map =>
        val path = s"maps/$map/$map.json"
        getFromResource(path)
      } ~
      pathPrefix("maps") {
        getFromResourceDirectory("maps") //TODO: this will list directory contents
      } ~
      path("sprites") {

        import scala.collection.JavaConverters._

        def toKey(p: Path) = p.getFileName.toString.split('.')(0)

        val spritesDir = Paths.get(this.getClass.getResource("/sprites").toURI)
        val sprites = Files.newDirectoryStream(spritesDir, "*.png").asScala.map(toKey)
        complete(upickle.write(sprites))
      } ~
      pathPrefix("sprites") {
        getFromResourceDirectory("sprites")
      } ~
      pathPrefix("public") {
        getFromResourceDirectory("public")
      }
    }
  }
}