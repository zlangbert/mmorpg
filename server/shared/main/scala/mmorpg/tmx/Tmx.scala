package mmorpg.tmx

import upickle.{Js, key}

import scala.collection.immutable

object Tmx {

  /**
   *
   * @param version
   * @param orientation
   * @param width
   * @param height
   * @param renderOrder
   * @param tilesets
   * @param layers
   */
  case class Map(version: Int, orientation: String,
                 width: Int, height: Int,
                 @key("renderorder") renderOrder: String,
                 tilesets: Seq[Tileset],
                 layers: Seq[Layer]) {

    val tileLayers = layers.collect {
      case l: TileLayer => l
    }

    val objectLayers = layers.collect {
      case l: ObjectLayer => l
    }

    //private val collisionLayer = layers.head //layers.find(_.name == "collision")

    /**
     * Checks for a collision tile at the given index
     * @param tileIndex The tileIndex
     * @return true if the tile exists on the collision layer
     */
    def isSolid(tileIndex: Int): Boolean = ???
      /*tileIndex < 0 ||
      collisionLayer.exists(_.data(tileIndex) != 0)*/
  }

  /**
   *
   * @param name
   * @param firstGid
   * @param tileWidth
   * @param tileHeight
   * @param imagePath
   * @param imageWidth
   * @param imageHeight
   * @param tileInfos
   */
  case class Tileset(name: String,
                     @key("firstgid") firstGid: Int,
                     @key("tilewidth") tileWidth: Int,
                     @key("tileheight") tileHeight: Int,
                     @key("image") imagePath: String,
                     @key("imagewidth") imageWidth: Int,
                     @key("imageheight") imageHeight: Int,
                     @key("tiles") tileInfos: Seq[TileInfo])

  /**
   *
   * @param id
   * @param animations
   */
  case class TileInfo(id: Int, animations: Seq[TileAnimation])

  /**
   * Custom TileInfo reader because of the slightly annoying way Tiled
   * formats the json
   */
  object TileInfo {
    implicit val reader = upickle.Reader[Seq[TileInfo]] {
      case Js.Obj(values@_*) =>
        values.map { case (id, data) =>
          val animations = upickle.readJs[Seq[TileAnimation]](data("animation"))
          TileInfo(id.toInt, animations)
        }
    }
  }

  /**
   *
   * @param duration
   * @param tileId
   */
  case class TileAnimation(duration: Int, @key("tileid") tileId: Int)

  /**
   * Layer supertype
   */
  sealed trait Layer

  /**
   * Custom reader for layer to handle the multiple layer types
   */
  object Layer {
    implicit val reader = upickle.Reader[Layer] {
      case o: Js.Obj => o("type") match {
        case Js.Str("tilelayer") =>
          val tagged = Js.Arr(Js.Str("mmorpg.tmx.Tmx.TileLayer"), o)
          upickle.readJs[TileLayer](tagged)
        case Js.Str("objectgroup") =>
          val tagged = Js.Arr(Js.Str("mmorpg.tmx.Tmx.ObjectLayer"), o)
          upickle.readJs[ObjectLayer](tagged)
        case _ => throw new Exception("unknown layer type")
      }
      case _ => throw new Exception("parsing map layer: unexpected json")
    }
  }

  /**
   *
   * @param name
   * @param data
   * @param width
   * @param height
   * @param opacity
   * @param visible
   */
  case class TileLayer(name: String,
                   data: Array[Int],
                   width: Int,
                   height: Int,
                   opacity: Double,
                   visible: Boolean) extends Layer

  /**
   *
   * @param name
   * @param width
   * @param height
   * @param objects
   */
  case class ObjectLayer(name: String, width: Int, height: Int, objects: Seq[TmxObject]) extends Layer

  /**
   *
   * @param gid
   */
  case class TmxObject(gid: Int)

}