package mmorpg.tmx

import scala.concurrent.Future

private[tmx] trait TmxLoader {
  def load(source: String): Tmx.Map
}

object TmxLoader {

  /**
   * Loads a map file
   * @param key The map key
   * @return The parsed map as a TmxMap
   */
  def load(key: String): Future[Tmx.Map] = PlatformTmxLoader.load(key)
}