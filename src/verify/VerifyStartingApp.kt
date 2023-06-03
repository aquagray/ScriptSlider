package verify

import preference.Preference
import script.Scene
import ui.ScriptPanel
import ui.UiHelper

class VerifyStartingApp : AutomaticRecordingApp() {
  val listener = object: ScriptPanel.KeyListener {
    override fun onPress(direction: ScriptPanel.KeyListener.Direction) {
      when (direction) {
        ScriptPanel.KeyListener.Direction.LEFT, ScriptPanel.KeyListener.Direction.RIGHT -> {
          Preference.saveCurrentPreferences(frame.width, frame.height)
          startRecording()
        }

        ScriptPanel.KeyListener.Direction.TOP -> {
          UiHelper.increaseTextSize()
          panel.updateScriptToCurrentTop()
          panel.next()
        }

        ScriptPanel.KeyListener.Direction.BOTTOM -> {
          UiHelper.decreaseTextSize()
          panel.updateScriptToCurrentTop()
          panel.next()
        }
      }
    }
  }

  override fun setupUi(scene: Scene) {
    super.setupUi(scene)
    panel.listener = listener
  }
}