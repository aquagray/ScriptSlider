package verify

import preference.Preference
import script.Scene

fun main(args: Array<String>) {
  VerifyStartingApp().apply {
      saveBreakPoints = { Preference.saveBreakPoints(it, Preference.SCENE1_BREAKS) }
      setupUi(Scene.Scene1)
      onFinish = {
        runScene(Scene.Scene2).apply {
          onFinish = {
            runScene(Scene.Scene3).apply {
              onFinish = {
                runScene(Scene.Scene4).apply {
                  onFinish = { println("DONE!") }
                }
              }
            }
          }
        }
      }
    }
}

private fun runScene(scene: Scene): AutomaticRecordingApp {
  val breaks = when (scene) {
    Scene.Scene1 -> Preference.SCENE1_BREAKS
    Scene.Scene2 -> Preference.SCENE2_BREAKS
    Scene.Scene3 -> Preference.SCENE3_BREAKS
    Scene.Scene4 -> Preference.SCENE4_BREAKS
    Scene.SceneAll -> throw RuntimeException()
  }
  return AutomaticRecordingApp().apply {
    saveBreakPoints = { Preference.saveBreakPoints(it, breaks) }
    start(scene)
  }
}