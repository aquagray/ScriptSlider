import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import java.awt.Font
import java.awt.GridBagConstraints.BOTH
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

fun main(args: Array<String>) {
    ScriptReaderApp().start("res/script.txt")
}
var screenSize: Dimension? = null

class ScriptReaderApp {
    fun start(scriptPath: String) {
        val f = JFrame("ScriptReader")
        screenSize = Toolkit.getDefaultToolkit().screenSize // 1680 x 1050
        val p = ControlPanel(scriptPath)
        p.isFocusable = true
        f.contentPane = p
        f.setBounds(0, 0, screenSize!!.width, screenSize!!.height)
        f.isVisible = true
        p.requestFocus()
    }
}

class ControlPanel : JPanel {
    var mainDisplay: MainDisplayImagePanel? = null

    var lineNumber = 3

    constructor(imgPath: String) : super() {

        mainDisplay = MainDisplayImagePanel(imgPath)
        add(mainDisplay)

        val actorSpace = screenSize!!.width / 6
        val lineSpace = (screenSize!!.width / 6) * 3

        addMouseListener(object : BaseMouseListener() {
            override fun mouseClicked(e: MouseEvent?) {
                mainDisplay!!.goForward(actorSpace, lineSpace)
            }
        })

        addKeyListener(object : BaseKeyListener() {
            override fun keyPressed(e: KeyEvent?) {
                when(e?.keyCode) {
                    KeyEvent.VK_LEFT -> mainDisplay!!.goBackward(actorSpace, lineSpace)
                    KeyEvent.VK_RIGHT -> mainDisplay!!.goForward(actorSpace, lineSpace)
                    KeyEvent.VK_UP -> {
                        remove(0)
                        lineNumber++
                        val currLine = mainDisplay!!.reader.curr
                        mainDisplay = MainDisplayImagePanel(imgPath, lineNumber, currLine)
                        add(mainDisplay)
//                        repaint()
                        // Force refresh
                        mainDisplay!!.goForward(actorSpace, lineSpace)
                        mainDisplay!!.goBackward(actorSpace, lineSpace)
                        repaint()
                    }
                    KeyEvent.VK_DOWN -> {
                        remove(0)
                        lineNumber--
                        val currLine = mainDisplay!!.reader.curr
                        mainDisplay = MainDisplayImagePanel(imgPath, lineNumber, currLine)
                        add(mainDisplay)
//                        repaint()
                        // Force refresh
                        mainDisplay!!.goForward(actorSpace, lineSpace)
                        mainDisplay!!.goBackward(actorSpace, lineSpace)
                        repaint()
                    }
                }
            }
        })
    }
}

class MainDisplayImagePanel : JPanel {

    val reader = ScriptReader()

    val numberOfLines: Int
    val list = ArrayList<JLabel>()
    val lineNumber = JLabel()

    constructor(imgPath: String, numberOfLines: Int = 3, currLine: Int = 0) : super() {

        this.numberOfLines = numberOfLines
        reader.load(imgPath)
        reader.goto(currLine)
        layout = GridBagLayout()
        val fontSize = 50
        val fontFamily = "Arial"
        val fontStyle = Font.BOLD

        for (i in 0 until numberOfLines) {
            val actor = JLabel()
            val line = JLabel()
            actor.font = Font(fontFamily, fontStyle, fontSize)
            actor.isOpaque = true
            line.font = Font(fontFamily, fontStyle, fontSize)
            line.isOpaque = true
            list.add(actor)
            list.add(line)
        }

        var y = 0
        for (i in 0 until numberOfLines) {
            val actor = list[i * 2]
            val line = list[i * 2 + 1]

            add(actor, constants(0, i, 1, 1, 1.0, 1.0))
            add(line, constants(1, i, 1, 1, 3.0, 1.0))
            y++
        }
        add(lineNumber, constants(1, y, 1, 1, 0.0, 0.0))
    }

    fun goForward(actorSpace: Int, lineSpace: Int) {
        for (i in 0 until numberOfLines) {
            val actor = list[i * 2]
            val line = list[i * 2 + 1]

            actor.text = "<html><body width='${actorSpace}px'><center>${reader.actor()}</center></body></html>"
            actor.background = reader.getColor(reader.actor())
            line.text = "<html><body width='${lineSpace}px'>${reader.line()}</body></html>"
            reader.nextLine()
        }
        lineNumber.text = reader.lineNumber()
    }

    fun goBackward(actorSpace: Int, lineSpace: Int) {
        val backIndex = Math.max(0, reader.curr - numberOfLines * 2)
        reader.goto(backIndex)
        for (i in 0 until numberOfLines) {
            val actor = list[i * 2]
            val line = list[i * 2 + 1]

            actor.text = "<html><body width='${actorSpace}px'><center>${reader.actor()}</center></body></html>"
            actor.background = reader.getColor(reader.actor())
            line.text = "<html><body width='${lineSpace}px'>${reader.line()}</body></html>"
            reader.nextLine()
        }
        lineNumber.text = reader.lineNumber()
    }

    val c = GridBagConstraints()

    fun constants(x: Int, y: Int, w: Int, h: Int, weightx: Double, weighty: Double): GridBagConstraints {
        c.fill = BOTH
        c.gridx = x
        c.gridy = y
        c.gridwidth = w
        c.gridheight = h
//        c.weightx = weightx
//        c.weighty = weighty
        c.ipadx = 100
        c.ipady = 100
        c.anchor = GridBagConstraints.CENTER
        return c
    }
}

open class BaseMouseListener: MouseListener {
    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }
}

open class BaseKeyListener: KeyListener {
    override fun keyTyped(e: KeyEvent?) {
    }

    override fun keyPressed(e: KeyEvent?) {
    }

    override fun keyReleased(e: KeyEvent?) {
    }
}