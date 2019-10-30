import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.*
import java.awt.Font
import java.awt.GridBagConstraints.BOTH


fun main(args: Array<String>) {
    ScriptReaderApp().start("res/script.txt")
}

class ScriptReaderApp {
    fun start(scriptPath: String) {
        val f = JFrame("ScriptReader")
        val p = MainDisplayImagePanel(scriptPath)
        f.contentPane = p
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        f.setBounds(0, 0, screenSize.width/2, screenSize.height/2)
        f.isVisible = true
    }
}

class MainDisplayImagePanel : JPanel {

    val reader = ScriptReader()
    val actor1 = JLabel()
    val colin = JLabel(":")
    val line1 = JLabel()

    val actor2 = JLabel()
    val line2 = JLabel()

    constructor(imgPath: String) : super() {

        reader.load(imgPath)
        layout = GridBagLayout()
        val fontSize = 60
        val fontFamily = "Serif"
        val fontStyle = Font.BOLD
        actor1.font = Font(fontFamily, fontStyle, fontSize)
        actor1.isOpaque = true
        colin.font = Font(fontFamily, fontStyle, fontSize)
        line1.font = Font(fontFamily, fontStyle, fontSize)
        line1.isOpaque = true

        actor2.font = Font(fontFamily, fontStyle, fontSize)
        actor2.isOpaque = true
        line2.font = Font(fontFamily, fontStyle, fontSize)
        line2.isOpaque = true

        // DOESN"T WORK
        add(actor1, constants(0, 0, 1, 1))

        add(line1, constants(2, 0, 9, 1))

        add(actor2, constants(0, 1, 1, 1))

        add(line2, constants(2, 1, 9, 1))

        addMouseListener(object : BaseMouseListener() {
            override fun mouseClicked(e: MouseEvent?) {
                actor1.text = reader.actor()
                actor1.background = Color.blue
                line1.text = reader.line()
                line1.background = Color.red
                println("clicked. actor ${actor1.text} && line ${line1.text}")

                if (reader.hasNext()) {
                    reader.nextLine()
                    actor2.text = reader.actor()
                    actor2.background = Color.lightGray
                    line2.text = reader.line()
                    line2.background = Color.CYAN
                }
            }
        })
    }
    val c = GridBagConstraints()

    fun constants(x: Int, y: Int, w: Int, h: Int): GridBagConstraints {
        c.fill = BOTH
        c.gridx = 0
        c.gridy = y
        c.gridwidth = w
        c.gridheight = h
        c.anchor = GridBagConstraints.WEST
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