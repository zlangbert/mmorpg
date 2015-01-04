package mmorpg.gfx

class Animation {

  def offset: (Int, Int) = {
    val index = 40
    val x = index % 5 * 96
    val y = index / 5 * 96
    x -> y
  }
}