package mmorpg.tmx

import java.nio.charset.Charset
import java.nio.file.{Files, Path}

private[tmx] class TmxJsonLoader extends TmxLoader {
  override def load(path: Path): Tmx.Map = {
    val content = new String(Files.readAllBytes(path), Charset.forName("UTF-8"))
    upickle.read[Tmx.Map](content)
  }
}