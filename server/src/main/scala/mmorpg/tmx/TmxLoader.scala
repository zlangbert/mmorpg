package mmorpg.tmx

import java.nio.file.Path

private[tmx] trait TmxLoader {
  def load(path: Path): Tmx.Map
}

object TmxLoader {

  /**
   * Loads a map file
   * @param path The path to the map file
   * @return The parsed map as a TmxMap
   */
  def load(path: Path): Tmx.Map = {
    // I could pull in a lib to do better file detection. Not sure if worth it
    val loader: TmxLoader = path.getFileName.toString match {
      case s if s.contains("json") =>
        new TmxJsonLoader
      case f@_ => throw new RuntimeException(s"Tried to load unsupported tmx format: $f")
    }
    loader.load(path)
  }
}