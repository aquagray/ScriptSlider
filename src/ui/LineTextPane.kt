package ui

import script.Line
import script.Script
import javax.swing.JTextPane

class LineTextPane(
  val script: Script,
  var currentLine: Line = script.line()): JTextPane() {

  init {
    isEditable = false
    isFocusable = false
    contentType = "text/html"
    // println("Text size : ${UiHelper.fontSizePx}")
    text = toHtml(script, currentLine)
    // This line count doesn't work.
    // val linecount = document.defaultRootElement.elementCount
  }

  fun updateTextSize() {
    text = toHtml(script, currentLine)
    repaint()
  }

  fun updateText() {
    currentLine = script.line()
    text = toHtml(script, currentLine)
    repaint()
  }

  private fun toHtml(script: Script, line: Line): String {
    val builder = StringBuilder()
    builder.append("<html><body>")

    builder.append(UiHelper.toHtml(script, currentLine))

    builder.append("</body></html>")
    return builder.toString()
  }
}