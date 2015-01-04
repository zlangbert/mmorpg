package mmorpg.tmx

import upickle.key

object Tmx {

  case class Map(version: Int, orientation: String,
                 width: Int, height: Int,
                 @key("renderorder") renderOrder: String,
                 tilesets: Seq[TileSet])

  case class TileSet(name: String,
                     @key("firstgid") firstGid: Int,
                     @key("tilewidth") tileWidth: Int,
                     @key("tileheight") tileHeight: Int,
                     @key("image") imagePath: String,
                     @key("imagewidth") imageWidth: Int,
                     @key("imageheight") imageHeight: Int)

  case class Layer(name: String,
                   data: Array[Int],
                   width: Int,
                   height: Int,
                   opacity: Double,
                   visible: Boolean)

}
