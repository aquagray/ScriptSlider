package ui

import script.Script
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.KeyEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.SwingUtilities


class ScriptPanel(var script: Script): JPanel() {

  val refreshExecutor = Executors.newSingleThreadScheduledExecutor()

  val list: MutableList<LineTextPane>

  var lastList = mutableListOf<LineTextPane>()

  var listener: KeyListener? = null

  var eventListener: EventListener? = null

  interface KeyListener {
    enum class Direction {LEFT, TOP, RIGHT, BOTTOM}
    fun onPress(direction: Direction)
  }

  interface EventListener {
    fun onFinishedRendering()
  }

  init {
    // scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    preferredSize = Toolkit.getDefaultToolkit().screenSize // 1680 x 1050

    list = UiHelper.createSingleLineTextPanes(script, preferredSize.height)
    for (text in list) {
      add(text)
    }

    println("adding key listener")
    addKeyListener(object : BaseKeyListener() {
      override fun keyPressed(e: KeyEvent?) {
        when(e?.keyCode) {
          KeyEvent.VK_LEFT -> {
            listener?.onPress(KeyListener.Direction.LEFT)
            // println("left")
            // useLastList()
          }
          KeyEvent.VK_RIGHT -> {
            listener?.onPress(KeyListener.Direction.RIGHT)
            // println("top")
            // next()
          }
          KeyEvent.VK_UP -> {
            listener?.onPress(KeyListener.Direction.TOP)
            // println("up")
            // UiHelper.increaseTextSize()
            // script.goto(list.first().currentLine)
            // next()
          }
          KeyEvent.VK_DOWN -> {
            listener?.onPress(KeyListener.Direction.BOTTOM)
            // println("down")
            // UiHelper.decreaseTextSize()
            // next()
          }
        }

      }
    })
    println("key listener added.")
  }

  fun updateScriptToCurrentTop() {
    script.goto(list.first().currentLine)
  }

  fun next() {
    lastList = list.toMutableList()
    list.clear()
    removeAll()

    list.addAll(UiHelper.createSingleLineTextPanes(script, preferredSize.height))
    for (text in list) {
      add(text)
    }
    refresh()
  }

  private fun useLastList() {
    if (lastList.isEmpty()) {
      // no op.
      return
    }
    list.clear()
    list.addAll(lastList.toMutableList())
    removeAll()
    for (text in list) {
      add(text)
    }
    lastList.clear()
    script.goto(list.last().currentLine)
    script.nextLine()
    refresh()
  }

  val pos = Point()
  private fun LineTextPane.bottom(): Int {
    getLocation(pos)
    return pos.y + height
  }

  private fun over(): Boolean {
    if (list.isEmpty()) { return false }
    val lastBottom = list.last().bottom()
    return lastBottom > height
  }

  var cutRetried = 0
  fun cut() {
    var cut = false
    while (over()) {
      // need to remove.
      val last = list.removeLast()
      remove(last)
      script.prevLine()
      println("removed : ${script.line().line}")
      cut = true
      cutRetried = 0
    }

    if (cutRetried >= 5) {
      cutRetried = 0
      return
    }

    if (!cut) {
      cutRetried++
      refresh()
      return
    }
    invalidate()
    revalidate()
    repaint()
  }

  private fun refresh() {
    invalidate()
    revalidate()
    repaint()

    // dumb but only way to ensure drawing completed..
    refreshExecutor.schedule(
      {
        SwingUtilities.invokeLater{
          cut()
        }}, 100, TimeUnit.MILLISECONDS)
  }

  companion object {
    private const val BOTTOM_PADDING_PX = 20;
  }
}
