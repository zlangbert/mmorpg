package mmorpg.tmx

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files

import scala.concurrent.Future

object PlatformTmxLoader {

  def load(key: String): Future[Tmx.Map] = {
    val p = s"/maps/$key/$key.json"
    val path = new File(getClass.getResource(p).toURI).toPath
    // I could pull in a lib to do better file detection. Not sure if worth it
    val loader: TmxLoader = path.getFileName.toString match {
      case s if s.contains("json") =>
        new TmxJsonLoader
      case f@_ => throw new RuntimeException(s"Tried to load unsupported tmx format: $f")
    }
    val source = new String(Files.readAllBytes(path), Charset.forName("UTF-8"))
    Future.successful(loader.load(source))
  }
}