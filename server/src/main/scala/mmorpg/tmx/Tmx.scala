package mmorpg.tmx

object Tmx {

  case class Map(version: Int, orientation: String, width: Int, height: Int, renderorder: String, tilesets: Seq[TileSet])

  case class TileSet(name: String)
}