package script
import Util.fits
import Util.parseActorAndLine
import java.awt.Color
import java.io.File
import script.ScriptPrep.actorsColorMap

val SKIP = setOf("<>")//"TITLE", "SCENE")

class Script(scene: Scene) {
    var curr = 0
    val lines = mutableListOf<Line>()
    /** Pure list of actors without any () or []. */
    val actorsNameOnly = mutableSetOf<String>()

    init {
        if (scene != Scene.SceneAll && actorsColorMap.isEmpty()) {
            // SceneAll will always have empty actorsColorMap since it's used to
            // build color map.
            println("================= starting new ===============")
            ScriptPrep.fillActorsColorMap()
        }
        val file = File(scene.filePath)
        parseScript(file)
    }

    fun lineNumber(): String {
        return "$curr / ${lines.size}"
    }

    private fun parseScript(file: File) {
        var index = 0
        file.forEachLine { lineFromFile ->
            if (fits(SKIP, lineFromFile)) {
                return@forEachLine
            }
            if (lineFromFile.isBlank()) {
                return@forEachLine
            }

            val parsedLine = parseActorAndLine(lineFromFile, index)
            lines.add(parsedLine)
            if (parsedLine.type == LineType.DIALOG) {
                actorsNameOnly.add(parsedLine.actorNameOnly)
            }
            index++
        }
    }

    fun printActors() {
        for (actor in actorsNameOnly) {
            println(actor)
        }
    }

    fun getColor(line: Line): Pair<Color, Color> {
        return ScriptPrep.getColors(line)

    }

    fun hasNext(): Boolean {
        return curr < lines.size - 1
    }

    fun nextLine() {
        if (curr < lines.size - 1) {
            curr++
        }
    }

    fun peekPrevLine(): Line? {
        return if (curr >= 1) {
            lines[curr - 1]
        } else {
            null
        }
    }
    fun prevLine() {
        if (curr >= 1) {
            curr--
        }
    }

    fun line(): Line {
        return lines[curr]
    }

    fun goto(i: Int) {
        curr = i
    }

    fun index(line: Line): Int {
        return lines.indexOf(line)
    }

    fun goto(line: Line) {
        val newIndex = lines.indexOf(line)
        if (newIndex != -1) {
            curr = newIndex
        }
    }
}

data class Line(
    val actor: String,
    val line: String,
    val type: LineType,
    val index: Int,
    var actorNameOnly: String = ""
) {
    init {
        actorNameOnly = actorNameOnly(original = actor)
    }
    private fun actorNameOnly(original: String ): String {
        return when (type) {
            LineType.DIALOG -> original.replace("[", "]")
            LineType.ACTION -> original.replace("(", ")")
            LineType.POSITION, LineType.ELSE -> original
        }
    }
}

enum class LineType { DIALOG, ACTION, POSITION, ELSE }
