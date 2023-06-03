import script.Line
import script.LineType

object Util {
  fun parseActorAndLine(line: String, index: Int): Line {
    if (line.startsWith("(Position)")) {
      val startIndex = line.indexOf('(')
      val endIndex = line.indexOf(')')
      return separate(startIndex, endIndex, line, LineType.POSITION, index)
    } else if (line.startsWith("(") && line.contains(")")) {
      val startIndex = line.indexOf('(')
      val endIndex = line.indexOf(')')
      return separate(startIndex, endIndex, line, LineType.ACTION, index)
    } else if (line.startsWith("[") && line.contains("]")) {
      val startIndex = line.indexOf('[')
      val endIndex = line.indexOf(']')
      return separate(startIndex, endIndex, line, LineType.DIALOG, index)
    }

    val startIndex = 0
    val endIndex = line.indexOfFirst { it == ' ' }
    return separate(startIndex, endIndex, line, LineType.ELSE, index)
  }

  private fun separate(start: Int, end: Int, line: String, type: LineType, index: Int): Line {
    val inside = line.substring(start, end + 1).trim()
    val remaining = line.substring(end + 1).trim()
    return Line(
      actor = inside,
      line = remaining,
      type = type,
      index = index
      )
  }

  fun fits(sets: Set<String>, line: String): Boolean {
    sets.forEach { set ->
      if (line.contains(set)) {
        return true
      }
    }
    return false
  }

}