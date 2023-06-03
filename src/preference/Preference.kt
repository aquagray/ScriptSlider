package preference

import java.io.File
import java.io.FileWriter
import ui.UiHelper

object Preference {
  private const val FILE = "preference/preferences.txt"

  private const val FONT = "font="
  private const val WIDTH = "width="
  private const val HEIGHT = "height="
  const val SCENE1_BREAKS = "scene1_breaks="
  const val SCENE2_BREAKS = "scene2_breaks="
  const val SCENE3_BREAKS = "scene3_breaks="

  const val FRAME_SIZE_FILE = "preference/frame_size.txt"

  fun saveCurrentPreferences(width: Int, height: Int) {
    val font = UiHelper.fontPx
    val file = File(FILE)
    file.writeText("${FONT}$font\n")
    FileWriter(file, true).use { writer ->
      writer.appendLine("${WIDTH}${width}")
      writer.appendLine("${HEIGHT}${height}")
    }
  }

  fun saveBreakPoints(breakpoints: List<Int>, field: String = SCENE1_BREAKS) {
    val file = File(FILE)
    FileWriter(file, true).use { writer ->
      writer.appendLine("${field}${breakpoints.joinToString(",")}")
    }
  }

  data class Preferences(
    var width: Int = -1,
    var height: Int = -1,
    val scene1_breaks: MutableList<Int> = mutableListOf(),
    val scene2_breaks: MutableList<Int> = mutableListOf(),
    val scene3_breaks: MutableList<Int> = mutableListOf(),
  )
  fun readPreferences() {
    val preferences = Preferences()
    File(FILE).forEachLine { line ->
      if (line.contains(FONT)) {
        val font = line.substringAfter(FONT).trim()
        UiHelper.fontPx = font.toInt()
      }
      else if (line.contains(WIDTH)) {
        val width = line.substringAfter(WIDTH).trim()
        preferences.width = width.toInt()
      }
      else if (line.contains(HEIGHT)) {
        val height = line.substringAfter(HEIGHT).trim()
        preferences.height = height.toInt()
      }
      else if (line.contains(SCENE1_BREAKS)) {
        // 1, 2, 3, 4, 5
        val array = line.substringAfter(HEIGHT).trim().split(",")
        preferences.scene1_breaks.addAll(array.map { it.trim().toInt() })
      }
      else if (line.contains(SCENE2_BREAKS)) {
        // 1, 2, 3, 4, 5
        val array = line.substringAfter(HEIGHT).trim().split(",")
        preferences.scene1_breaks.addAll(array.map { it.trim().toInt() })
      }
      else if (line.contains(SCENE3_BREAKS)) {
        // 1, 2, 3, 4, 5
        val array = line.substringAfter(HEIGHT).trim().split(",")
        preferences.scene1_breaks.addAll(array.map { it.trim().toInt() })
      }
    }
  }

  fun saveFrameSize(width: Int, height: Int) {

  }

  fun readFontSize() {
    val fontFile = File(FILE)
    val font = fontFile.readText()
    UiHelper.fontPx = font.toInt()
  }
}