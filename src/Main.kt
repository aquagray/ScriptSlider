import java.awt.*
import javax.swing.*
import script.Scene
import script.Script
import ui.ScriptPanel

fun main(args: Array<String>) {
    ScriptReaderApp().start(Scene.SceneAll)
}
var screenSize: Dimension? = null

class ScriptReaderApp {
    fun start(scene: Scene) {
        paneWorks(scene)
    }

    private fun paneWorks(scene: Scene) {
        val frame = JFrame("Scrollable HTML Content")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val script = Script(scene)
        val panel = ScriptPanel(script)

        frame.contentPane.add(panel)
        frame.pack()
        panel.requestFocus()
        frame.isVisible = true

        SwingUtilities.invokeLater {
            println("Force refit afterwards. calculation lacked inset.")
            panel.cut()
        }
    }

    // private fun scrollingWorks(scriptPath: String) {
    //     val frame = JFrame("Scrollable HTML Content")
    //     frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    //
    //     val script = Script(scriptPath)
    //     val textPane = SingleScriptTextPane(script)
    //
    //     // TextPane must be passed as constructor, otherwise it won't work.
    //     val scrollPane = ScriptScrollPanel(textPane)
    //     scrollPane.preferredSize = Toolkit.getDefaultToolkit().screenSize // 1680 x 1050
    //
    //     frame.contentPane.add(scrollPane)
    //     frame.pack()
    //     scrollPane.requestFocus()
    //     frame.isVisible = true
    // }
}
//
// class ControlPanel : JPanel {
//     var mainDisplay: MainDisplayImagePanel? = null
//     var lineNumber = 3
//
//     constructor(scriptPath: String) : super() {
//
//         mainDisplay = MainDisplayImagePanel(scriptPath)
//         add(mainDisplay)
//
//         val actorSpace = screenSize!!.width / 6
//         val lineSpace = (screenSize!!.width / 6) * 3
//
//         addMouseListener(object : BaseMouseListener() {
//             override fun mouseClicked(e: MouseEvent?) {
//                 mainDisplay!!.goForward(actorSpace, lineSpace)
//             }
//         })
//
//         addKeyListener(object : BaseKeyListener() {
//             override fun keyPressed(e: KeyEvent?) {
//                 when(e?.keyCode) {
//                     KeyEvent.VK_LEFT -> mainDisplay!!.goBackward(actorSpace, lineSpace)
//                     KeyEvent.VK_RIGHT -> mainDisplay!!.goForward(actorSpace, lineSpace)
//                     KeyEvent.VK_UP -> {
//                         remove(0)
//                         lineNumber++
//                         val currLine = mainDisplay!!.reader.curr
//                         mainDisplay = MainDisplayImagePanel(scriptPath, lineNumber, currLine)
//                         add(mainDisplay)
// //                        repaint()
//                         // Force refresh
//                         mainDisplay!!.goForward(actorSpace, lineSpace)
//                         mainDisplay!!.goBackward(actorSpace, lineSpace)
//                         repaint()
//                     }
//                     KeyEvent.VK_DOWN -> {
//                         remove(0)
//                         lineNumber--
//                         val currLine = mainDisplay!!.reader.curr
//                         mainDisplay = MainDisplayImagePanel(scriptPath, lineNumber, currLine)
//                         add(mainDisplay)
// //                        repaint()
//                         // Force refresh
//                         mainDisplay!!.goForward(actorSpace, lineSpace)
//                         mainDisplay!!.goBackward(actorSpace, lineSpace)
//                         repaint()
//                     }
//                 }
//             }
//         })
//     }
// }

// class MainDisplayImagePanel : JPanel {
//
//     val reader = Script()
//
//     val numberOfLines: Int
//     val list = ArrayList<JLabel>()
//     val lineNumber = JLabel()
//
//     constructor(scriptPath: String, numberOfLines: Int = 3, currLine: Int = 0) : super() {
//
//         this.numberOfLines = numberOfLines
//         reader.load(scriptPath)
//
//         reader.goto(currLine)
//         layout = GridBagLayout()
//         val fontSize = 50
//         val fontFamily = "Arial"
//         val fontStyle = Font.BOLD
//
//         for (i in 0 until numberOfLines) {
//             val actor = JLabel()
//             val line = JLabel()
//             actor.font = Font(fontFamily, fontStyle, fontSize)
//             actor.isOpaque = true
//             line.font = Font(fontFamily, fontStyle, fontSize)
//             line.isOpaque = true
//             list.add(actor)
//             list.add(line)
//         }
//
//         var y = 0
//         for (i in 0 until numberOfLines) {
//             val actor = list[i * 2]
//             val line = list[i * 2 + 1]
//
//             add(actor, constants(0, i, 1, 1, 1.0, 1.0))
//             add(line, constants(1, i, 1, 1, 3.0, 1.0))
//             y++
//         }
//         add(lineNumber, constants(1, y, 1, 1, 0.0, 0.0))
//     }
//
//     fun goForward(actorSpace: Int, lineSpace: Int) {
//         for (i in 0 until numberOfLines) {
//             val actor = list[i * 2]
//             val line = list[i * 2 + 1]
//
//             actor.text = "<html><body width='${actorSpace}px'><center>${reader.actor()}</center></body></html>"
//             actor.background = reader.getColor(reader.actor())
//             line.text = "<html><body width='${lineSpace}px'>${reader.line()}</body></html>"
//             reader.nextLine()
//         }
//         lineNumber.text = reader.lineNumber()
//     }
//
//     fun goBackward(actorSpace: Int, lineSpace: Int) {
//         val backIndex = Math.max(0, reader.curr - numberOfLines * 2)
//         reader.goto(backIndex)
//         for (i in 0 until numberOfLines) {
//             val actor = list[i * 2]
//             val line = list[i * 2 + 1]
//
//             actor.text = "<html><body width='${actorSpace}px'><center>${reader.actor()}</center></body></html>"
//             actor.background = reader.getColor(reader.actor())
//             line.text = "<html><body width='${lineSpace}px'>${reader.line()}</body></html>"
//             reader.nextLine()
//         }
//         lineNumber.text = reader.lineNumber()
//     }
//
//     val c = GridBagConstraints()
//
//     fun constants(x: Int, y: Int, w: Int, h: Int, weightx: Double, weighty: Double): GridBagConstraints {
//         c.fill = BOTH
//         c.gridx = x
//         c.gridy = y
//         c.gridwidth = w
//         c.gridheight = h
// //        c.weightx = weightx
// //        c.weighty = weighty
//         c.ipadx = 100
//         c.ipady = 100
//         c.anchor = GridBagConstraints.CENTER
//         return c
//     }
// }
//
// open class BaseMouseListener: MouseListener {
//     override fun mouseReleased(e: MouseEvent?) {
//     }
//
//     override fun mouseEntered(e: MouseEvent?) {
//     }
//
//     override fun mouseClicked(e: MouseEvent?) {
//     }
//
//     override fun mouseExited(e: MouseEvent?) {
//     }
//
//     override fun mousePressed(e: MouseEvent?) {
//     }
// }
//
// open class BaseKeyListener: KeyListener {
//     override fun keyTyped(e: KeyEvent?) {
//     }
//
//     override fun keyPressed(e: KeyEvent?) {
//     }
//
//     override fun keyReleased(e: KeyEvent?) {
//     }
// }