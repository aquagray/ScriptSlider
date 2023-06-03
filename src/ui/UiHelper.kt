package ui

import script.Line
import script.LineType
import script.Script

object UiHelper {
  var fontPx = 50
  var spaceBetweenLinePx = 10

  fun increaseTextSize() {
    fontPx += 10
  }

  fun decreaseTextSize() {
    fontPx -= 10
  }

  /** Returns number of index (inclusive) that can fit in the height. */
  fun refit(
    script: Script,
    list: MutableList<LineTextPane>,
    limitHeightPx: Int
  ): Int {
    var allTextHeights = 0
    var index = 0
    while (limitHeightPx > allTextHeights && index < list.size - 1) {
      val text = list[index]
      allTextHeights += text.preferredSize.height
      index++
    }

    if (limitHeightPx > allTextHeights) {
      // more can fit!
      val more = createSingleLineTextPanes(script, limitHeightPx - allTextHeights)
      list.addAll(more)
      return list.size
    }
    return index - 1
  }

  fun createSingleLineTextPanes(
    script: Script,
    limitHeightPx: Int
  ): MutableList<LineTextPane> {
    val list = mutableListOf<LineTextPane>()
    var allTextHeights = 0

    var text: LineTextPane?
    do {
      text = LineTextPane(script = script)
      list.add(text)
      allTextHeights += text.preferredSize.height
      script.nextLine()
    } while (limitHeightPx > allTextHeights && script.hasNext())

    assert(text != null)
    assert(text?.currentLine == script.peekPrevLine())

    return list
  }

  fun singleLineHtml(): String {
    val builder = StringBuilder()
    builder.append("<html><body>")
    builder.append("""
      <p style='font-size: "${fontPx}"px;'>
      <b>Hello</b>&emsp;World!</p>
    </body></html>
    """.trimIndent())
    return builder.toString()
  }

  fun toHtml(script: Script): String {
    val builder = StringBuilder()
    builder.append("<html><body>")

    do {
      builder.append(toHtml(script, script.line()))
      script.nextLine()
    } while (script.hasNext())

    builder.append("</body></html>")
    return builder.toString()
  }

  fun toHtml(script: Script, line: Line): String {
    val colors = script.getColor(line)
    val actorHex = "#" + Integer.toHexString(colors.first.rgb).substring(2)
    val lineHex = "#" + Integer.toHexString(colors.second.rgb).substring(2)
    val fam = when (line.type) {
      LineType.DIALOG, LineType.ACTION -> "Arial"
      LineType.POSITION -> "'Times New Roman', Times, serif"
      LineType.ELSE -> "Arial"
    }
    // val lineToRead = when (line.type) {
    //   LineType.DIALOG, LineType.ACTION ->
    //   {
    //     // version 1.
    //     // val styleStart = "<span style=\"font-size: ${fontPx}px; font-family: ${fam};"
    //     // val styleEnd = "\">"
    //     // return """
    //     //   $styleStart background-color: ${hex};${styleEnd} ${line.actor}</span> &emsp;
    //     //   $styleStart ${styleEnd} ${line.line}</span>
    //     // """.trimIndent()
    //
    //     // version 2.
    //     // "<b>${line.actor}</b> &emsp; ${line.line}"
    //
    //     // version 3.
    //     """
    //       <span style="font-size: ${fontPx}px; font-family: ${fam}; background-color: ${actorHex};">
    //       <b>${line.actor}</b>
    //       </span> &emsp;
    //       <span style="font-size: ${fontPx}px; font-family: ${fam}; background-color: ${lineHex};">
    //       ${line.line}
    //       </span>
    //     """.trimIndent()
    //   }
    //   LineType.POSITION -> "&emsp;${line.line}"
    //   LineType.ELSE -> "${line.actor}&emsp;${line.line}"
    // }
    val lineToRead = """
          <span style="font-size: ${fontPx}px; font-family: ${fam}; background-color: ${actorHex};">
          <b>${line.actor}</b>
          </span> &emsp;
          <span style="font-size: ${fontPx}px; font-family: ${fam}; background-color: ${lineHex};">
          ${line.line}
          </span> 
        """.trimIndent()
    //<p style="font-size: ${fontPx}px; font-family: ${fam}; background-color: ${actorHex};">
    return """
      <p>
      $lineToRead
      </p>
      <br>
      <br>
    """.trimIndent()
  }
}