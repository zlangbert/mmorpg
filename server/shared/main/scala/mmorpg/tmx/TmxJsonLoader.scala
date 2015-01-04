package mmorpg.tmx

private[tmx] class TmxJsonLoader extends TmxLoader {

  override def load(source: String): Tmx.Map = upickle.read[Tmx.Map](source)
}