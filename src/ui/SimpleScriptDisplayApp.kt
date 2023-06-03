package ui

import java.awt.Dimension
import java.util.concurrent.TimeUnit
import javax.swing.JFrame
import javax.swing.SwingUtilities
import preference.Preference
import script.Scene
import script.Script

open class SimpleScriptDisplayApp {
  lateinit var panel: ScriptPanel
  lateinit var script: Script
  lateinit var frame: JFrame
  var onFinish: (() -> Unit)? = null
  var pref = Preference.Preferences()

  open val title = "Simple app"

  open fun setupUi(scene: Scene) {
    frame = JFrame(title)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    if (pref.width != -1 && pref.height != -1) {
      frame.preferredSize = Dimension(pref.width, pref.height)
    }

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
}
