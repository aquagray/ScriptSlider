package ui

import javax.swing.JScrollPane

class ScriptScrollPanel(val text: LineTextPane): JScrollPane(text) {
  init {
    verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
  }
}