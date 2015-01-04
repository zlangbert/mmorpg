package mmorpg.assets

object Asset {

  sealed trait AssetType
  case object Tileset extends AssetType
  case object Sprite extends AssetType
}