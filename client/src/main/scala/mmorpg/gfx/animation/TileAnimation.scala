package mmorpg.gfx.animation

import mmorpg.gfx.TimeDelta
import mmorpg.tmx.Tmx
import scala.collection.mutable

class TileAnimation(sequence: Seq[Tmx.TileAnimation]) extends Animation {

  /**
   * The tile id of the currently active animation frame
   * @return The local tile id
   */
  def tileId: Int = queue.head.tileId

  private val queue = mutable.Queue[Tmx.TileAnimation](sequence: _*)

  override def update(delta: TimeDelta) = {
    super.update(delta)

    time = time match {
      case next if next >= queue.head.duration =>
        queue.dequeue()
        if (queue.isEmpty) queue ++= sequence
        0
      case _ => time
    }
  }
}

object TileAnimation {

  /**
   * Creates a new TileAnimation
   * @param sequence The animation sequence
   * @return A new animation instance
   */
  def apply(sequence: Seq[Tmx.TileAnimation]) = new TileAnimation(sequence)
}