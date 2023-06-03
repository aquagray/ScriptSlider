package ui

import java.awt.Point
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.SwingUtilities
import preference.Preference.Preferences
import preference.Preference.toPairs
import script.Line
import script.Scene
import script.Script

class ScriptPanelWithPreference (var script: Script, val prefs: Preferences): JPanel() {

  var listener: KeyListener? = null

  val pairs = mutableListOf<Pair<Int, Int>>()
  var currentIndex = 0

  var pageNumber: String = ""
    private set

  interface KeyListener {
    enum class Direction {LEFT, TOP, RIGHT, BOTTOM}
    fun onPress(direction: Direction)
  }

  init {
    // scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    preferredSize = Toolkit.getDefaultToolkit().screenSize // 1680 x 1050

    pairs.addAll(when (script.scene) {
      Scene.Scene1 -> prefs.scene1_breaks.toPairs()
      Scene.Scene2 -> prefs.scene2_breaks.toPairs()
      Scene.Scene3 -> prefs.scene3_breaks.toPairs()
      Scene.Scene4 -> prefs.scene4_breaks.toPairs()
      Scene.SceneAll -> throw RuntimeException("Error")
    })

    addKeyListener(object : BaseKeyListener() {
      override fun keyPressed(e: KeyEvent?) {
        when(e?.keyCode) {
          KeyEvent.VK_LEFT -> {
            listener?.onPress(KeyListener.Direction.LEFT)
          }
          KeyEvent.VK_RIGHT -> {
            listener?.onPress(KeyListener.Direction.RIGHT)
          }
          KeyEvent.VK_UP -> {
            // Cannot control font size.
            // listener?.onPress(KeyListener.Direction.TOP)
          }
          KeyEvent.VK_DOWN -> {
            // Cannot control font size.
            // listener?.onPress(KeyListener.Direction.BOTTOM)
          }
        }

      }
    })
    displayCurrent()
  }

  fun displayCurrent() {
    val pair = pairs.get(currentIndex)
    val lines = script.get(pair.first, pair.second)
    display(lines, pair.first)
  }

  private fun display(lines: List<Line>, page: Int) {
    removeAll()
    for (text in lines) {
      add(LineTextPane(script = script, currentLine = text))
    }
    add(JTextPane().apply { text = "page $page / ${script.lines.size}" })
    pageNumber = "page $page / ${script.lines.size}"
    invalidate()
    revalidate()
    repaint()
  }

  fun prev() {
    if (currentIndex >= 1) {
      currentIndex--
    }
    displayCurrent()
  }

  fun next() {
    println("${currentIndex}, out of ${pairs.size}")
    if (currentIndex == pairs.size - 1) {
      // Last page is excluded in break point.
      display(listOf(script.lines.last()), script.lines.size)
      return
    } else if (currentIndex < pairs.size - 1) {
      currentIndex++
    }
    displayCurrent()
  }

  companion object {
    private const val BOTTOM_PADDING_PX = 20;
  }
}
