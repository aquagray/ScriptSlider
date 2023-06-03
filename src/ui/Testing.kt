package ui

import javax.swing.*
import java.awt.*
import javax.swing.text.JTextComponent

fun main() {
  SwingUtilities.invokeLater {
    val frame = JFrame("Scroll to Line")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    val textArea = JTextArea()
    textArea.isEditable = false
    textArea.text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10"

    val scrollButton = JButton("Scroll to Line 6")
    scrollButton.addActionListener {
      scrollToLine(textArea, 6)
    }

    val panel = JPanel(BorderLayout())
    panel.add(JScrollPane(textArea), BorderLayout.CENTER)
    panel.add(scrollButton, BorderLayout.SOUTH)

    frame.add(panel)
    frame.setSize(400, 300)
    frame.isVisible = true
  }
}

private fun scrollToLine(textComponent: JTextComponent, line: Int) {
  // try {
  //   val lineStartOffset = textComponent.getLineStartOffset(line - 1)
  //   val rect = textComponent.modelToView(lineStartOffset)
  //   if (rect != null) {
  //     textComponent.scrollRectToVisible(rect)
  //   }
  // } catch (e: BadLocationException) {
  //   e.printStackTrace()
  // }
}
