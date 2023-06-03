package verify

import preference.Preference
import script.Scene

fun main(args: Array<String>) {
  FontCheckStartApp()
    .apply {
      saveBreakPoints = { Preference.saveBreakPoints(it, Preference.SCENE1_BREAKS) }
      setupUi(Scene.Scene1)
      onFinish = {
        saveBreakPoints = { Preference.saveBreakPoints(it, Preference.SCENE2_BREAKS) }
        start(Scene.Scene2)
        onFinish = {
          saveBreakPoints = { Preference.saveBreakPoints(it, Preference.SCENE3_BREAKS) }
          start(Scene.Scene3)
          onFinish = { println("DONE!") }
        }
      }
    }
}