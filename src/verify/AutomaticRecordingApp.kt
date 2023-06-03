package verify

import java.util.concurrent.TimeUnit
import preference.Preference
import script.Scene
import ui.ScriptPanel
import ui.SimpleScriptDisplayApp

open class AutomaticRecordingApp : SimpleScriptDisplayApp() {

  var saveBreakPoints: ((List<Int>) -> Unit)? = null
  override val title: String = "Verify and save results"

  fun start(scene: Scene) {
    pref = Preference.readPreferences()
    super.setupUi(scene)
    scheduleStartRecording()
  }

  private fun scheduleStartRecording() {
    panel.refreshExecutor.schedule(
      {
        startRecording()
      }, 1500, TimeUnit.MILLISECONDS)
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
    println("verifying : ${script.scene.name}")
    // verify. Each pair of int should look like:
    // 1-3, 4-5, 6-9, 10-15 ... there shouldn't be any gab
    for (i in 0 until breakPoints.size step 2) {
      val first = breakPoints[i]
      val last = breakPoints[i + 1]

      breakPoints.getOrNull(i - 1)?.let { prevLast ->
        // previous last +1 is current first.
        verify(prevLast + 1 == first)
      }
      breakPoints.getOrNull(i + 2)?.let { nextFirst ->
        // current last + 1 is next first.
        verify(last + 1 == nextFirst)
      }
    }
    println("verifified!")
    // verify(breakPoints.last() == script.lines.size)
    // HACK
    if (breakPoints.last() + 1 != script.lines.size - 1) {
      val one = breakPoints.last() + 1
      val two = script.lines.size - 1
      breakPoints.add(one)
      breakPoints.add(two)
      println("HACK. Adding : ${one}, ${two}")
    }
    println("verifified!")
  }

  fun verify(statement: Boolean) {
    if (!statement) {
      throw RuntimeException("Error")
    }
  }

  private fun scheduleBreakpoint(panel: ScriptPanel, limit: () -> Boolean, finished: () -> Unit) {
    panel.refreshExecutor.schedule(
      {
        val limitReached = limit.invoke()
        // println("scheduled breakpoint - limit : ${limitReached}")
        breakPoints.add(panel.list.first().currentLine.index)
        breakPoints.add(panel.list.last().currentLine.index)
        panel.next()
        if (!limitReached) {
          scheduleBreakpoint(panel, limit, finished)
        } else {
          finished.invoke()
        }
      }, 1500, TimeUnit.MILLISECONDS)
  }
}
