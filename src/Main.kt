import java.awt.*
import javax.swing.*
import preference.Preference
import script.Scene
import script.Script
import ui.ScriptPanelWithPreference
import ui.ScriptPanelWithPreference.KeyListener.Direction

fun main(args: Array<String>) {
    val arg = args.get(0)
    when (arg) {
        "1" -> {
            ScriptReaderApp().start(Scene.Scene1)
        }
        "2" -> {
            ScriptReaderApp().start(Scene.Scene2)
        }
        "3" -> {
            ScriptReaderApp().start(Scene.Scene3)
        }
        "4" -> {
            ScriptReaderApp().start(Scene.Scene4)
        }
    }
    println(arg)
    // ScriptReaderApp().start(Scene.Scene4)
}

var screenSize: Dimension? = null

class ScriptReaderApp {
    fun start(scene: Scene) {
        paneWorks(scene)
    }

    private fun paneWorks(scene: Scene) {
        val preference = Preference.readPreferences()

        val frame = JFrame("Scrollable HTML Content")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        if (preference.height != -1 && preference.width != -1) {
            frame.preferredSize = Dimension(preference.width, preference.height)
        }

        val script = Script(scene)
        val panel = ScriptPanelWithPreference(script, Preference.readPreferences())
        panel.listener = object : ScriptPanelWithPreference.KeyListener {
            override fun onPress(direction: Direction) {
                when (direction) {
                    Direction.LEFT -> {
                        panel.prev()
                        frame.title = panel.pageNumber
                    }
                    Direction.RIGHT -> {
                        panel.next()
                        frame.title = panel.pageNumber
                    }
                    Direction.TOP, Direction.BOTTOM -> { }
                }
            }
        }

        frame.contentPane.add(panel)
        frame.pack()
        panel.requestFocus()
        frame.isVisible = true
    }
}