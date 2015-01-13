package mmorpg.tmx

import upickle.{Js, key}

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

    /**
     * The size of a tile in pixels
     */
    val tileSize = tilesets.head.tileWidth


    /**
     * The last tile index on this map
     */
    val lastIndex = width * height - 1


    /**
     * Renderable tile layers
     */
    val tileLayers = layers.collect {
      case l: TileLayer => l
    }

    /**
     * Information only object layers
     */
    val objectLayers = layers.collect {
      case l: ObjectLayer => l
    }

    /**
     * Calculates a tile index from world coordinates
     * @param x x coordinate
     * @param y y coordinate
     * @return The tile index
     */
    def indexFromCoords(x: Int, y: Int): Int = (y / tileSize) * height + (x / tileSize)

    /**
     * Checks for a collision tile at the given index
     * @param tileIndex The tileIndex
     * @return true if the tile exists on the collision layer
     */
    def isSolid(tileIndex: Int): Boolean =
      tileIndex < 0 || tileIndex > lastIndex ||
      collisionLayer.tileData(tileIndex).gid > 0

    private lazy val collisionLayer =
      tileLayers.find(_.name == "collision")
        .getOrElse(throw new Exception("failed to find map collision layer"))
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
    // upickle expects longs to be in quotes but they aren't in the map json
    implicit val longReader = upickle.Reader[Long] {
      case Js.Num(v) => v.toLong
    }
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
                   private val data: Array[Long],
                   width: Int,
                   height: Int,
                   opacity: Double,
                   visible: Boolean) extends Layer {

    val tileData = data.map(toTile)

    private def toTile(data: Long): TileLayer.TileData = {
      val FlippedHorizontallyFlag = 0x80000000l
      val FlippedVerticallyFlag = 0x40000000l
      val FlippedDiagonallyFlag = 0x20000000l

      val orientation = data match {
        case id if (id & FlippedHorizontallyFlag) != 0 => TileOrientation.FlippedHorizontally
        case id if (id & FlippedVerticallyFlag) != 0 => TileOrientation.FlippedVertically
        case id if (id & FlippedDiagonallyFlag) != 0 => TileOrientation.FlippedDiagonally
        case _ => TileOrientation.Default
      }

      val gid = data & ~(FlippedHorizontallyFlag | FlippedVerticallyFlag | FlippedDiagonallyFlag)

      TileLayer.TileData(gid.toInt, orientation)
    }
  }

  object TileLayer {
    case class TileData(gid: Int, orientation: TileOrientation.TileOrientation)
  }

  object TileOrientation {
    sealed trait TileOrientation
    case object Default extends TileOrientation
    case object FlippedHorizontally extends TileOrientation
    case object FlippedVertically extends TileOrientation
    case object FlippedDiagonally extends TileOrientation
  }

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
  case class TmxObject(gid: Long)

}