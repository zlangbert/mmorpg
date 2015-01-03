package mmorpg.assets

import java.io.File
import java.nio.file.Files
import java.util.Base64

object Assets {
  def load(): String = {
    val path = new File(getClass.getResource("/tilesheet.png").toURI).toPath
    val data = Files.readAllBytes(path)
    Base64.getEncoder.encodeToString(data)
  }
}