package verify

import script.Script
import java.util.concurrent.TimeUnit
import javax.swing.JFrame
import javax.swing.SwingUtilities
import script.Scene
import ui.ScriptPanel

open class FontCheckAppSimple {

  lateinit var panel: ScriptPanel
  lateinit var script: Script
  lateinit var frame: JFrame
  var onFinish: (() -> Unit)? = null
  var saveBreakPoints: ((List<Int>) -> Unit)? = null
  fun start(scene: Scene) {
    setupUi(scene)
    scheduleStartRecording()
  }

  open fun setupUi(scene: Scene) {
    frame = JFrame("Font check app.")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    script = Script(scene)
    panel = ScriptPanel(script)
    frame.contentPane.add(panel)
    frame.pack()
    panel.requestFocus()
    frame.isVisible = true

    SwingUtilities.invokeLater {
      println("Force refit afterwards. calculation lacked inset.")
      panel.cut()
    }
  }

  private fun scheduleStartRecording() {
    panel.refreshExecutor.schedule(
      {
        startRecording()
      }, 1000, TimeUnit.MILLISECONDS)
  }

  var breakPoints = mutableListOf<Int>()
  fun startRecording() {
    var i = 0
    scheduleBreakpoint(
      panel,
      limit = {
        !script.hasNext()
      },
      finished = {
        verifyNoGap()
        println("point : ${breakPoints.joinToString(",")}")
        saveBreakPoints?.invoke(breakPoints)
        breakPoints.clear()
        onFinish?.invoke()
      })
  }

  private fun verifyNoGap() {
    // verify. Each pair of int should look like:
    // 1-3, 4-5, 6-9, 10-15 ... there shouldn't be any gab
    for (i in 0 until breakPoints.size step 2) {
      val first = breakPoints[i]
      val last = breakPoints[i + 1]

      breakPoints.getOrNull(i - 1)?.let { prevLast ->
        // previous last +1 is current first.
        assert(prevLast + 1 == first)
      }
      breakPoints.getOrNull(i + 2)?.let { nextFirst ->
        // current last + 1 is next first.
        assert(last + 1 == nextFirst)
      }
    }
  }

  private fun scheduleBreakpoint(panel: ScriptPanel, limit: () -> Boolean, finished: () -> Unit) {
    panel.refreshExecutor.schedule(
      {
        val limitReached = limit.invoke()
        println("scheduled breakpoint - limit : ${limitReached}")
        breakPoints.add(panel.list.first().currentLine.index)
        breakPoints.add(panel.list.last().currentLine.index)
        panel.next()
        if (!limitReached) {
          scheduleBreakpoint(panel, limit, finished)
        } else {
          finished.invoke()
        }
      }, 1000, TimeUnit.MILLISECONDS)
  }
}
