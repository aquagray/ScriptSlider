import java.awt.Color
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

val DEBUG_MODE = false
val SKIP = listOf("[music starts]", "<instrumental>", "[song starts]", "[song begins]")

val ACTOR_COLOR = mapOf(
    "Aladdin" to Color(216, 233, 211),
    "Jafar" to Color(255, 228, 152),
    "Iago" to Color(234, 153, 153),
    "Jafar + Iago" to Color(213, 177, 178),
    "Genie" to Color(201, 218, 247),
    "Genie + Jafar" to Color(255, 242, 203),
    "Jasmine" to Color(199, 255, 238)
)

class ScriptReader {
    private val scriptList = ArrayList<String>()
    var curr = 0
    private val actors = HashSet<String>()

    fun lineNumber(): String {
        return "$curr / ${scriptList.size}"
    }

    fun load(path: String) {
        File(path).forEachLine {
            SKIP.forEach{skip ->
                if (it.contains(skip)) {
                    return@forEachLine
                }
            }
            if (it.isBlank()) {
                return@forEachLine
            }

            var stringToAdd = it
            val split = if (it.contains("\t"))  it.split("\t")
                        else it.split(" ")
            if (split[0].isNotEmpty()) {
                actors.add(split[0])
            } else {
                val prevActor = prevActor(scriptList.size - 1)
                if (prevActor.isEmpty()) {
                    println("ERROR: No actor found for line: $it")
                }

                if (DEBUG_MODE) {
                    println("ACTOR PARSING FAILED : $it. --- to new actor [$prevActor]")
                }
                stringToAdd = "$prevActor:\t$it"
            }

            scriptList.add(stringToAdd)
        }

        if (DEBUG_MODE) {
            println("Actors:")
            actors.forEach {
                println(it)
            }
        }
        curr = 0
    }

    fun printActors() {
        actors.forEach {
            println(it)
        }
    }

    fun getColor(actor: String): Color {
        return ACTOR_COLOR[actor] ?: Color.WHITE
    }

    fun hasNext(): Boolean {
        return curr < scriptList.size - 1
    }

    fun nextLine() {
        if (curr < scriptList.size - 1) {
            curr++
        }
    }

    fun debug() {
        // Find all the failed lines
        while (curr < scriptList.size - 1) {
            curr++
            val line = scriptList[curr]
            val currentActor = currActor(curr)
            if (currentActor.isEmpty()) {
                println(line)
            }
        }
    }

    fun actor(): String {
        if (curr >= scriptList.size) {
            return ""
        }

        val line = scriptList[curr]
        actors.forEach {
            if (line.startsWith(it)) {
                return it
            }
        }

        return prevActor()
    }

    private fun currActor(index: Int): String {
        val line = scriptList[index]
        actors.forEach {
            if (line.startsWith(it)) {
                return it
            }
        }
        return ""
    }

    private fun prevActor(index: Int = curr): String {
        var currTemp = index
        while (currTemp >= 0) {
            val actor = currActor(currTemp)
            if (actor.isNotEmpty()) {
                return actor
            }
            currTemp--
        }
        return ""
    }

    fun line(): String {
        if (curr >= scriptList.size) {
            return ""
        }

        val currActor = actor()
        val line = scriptList[curr]
        return line.substringAfter(currActor)
    }

    fun goto(i: Int) {
        curr = i
    }
}

// Test script reader.
fun main(args: Array<String>) {
    val reader = ScriptReader()
    reader.load("res/script.txt")

    reader.printActors()

//    reader.debug()

//    do {
//        println("Actor(${reader.actor()}) : ${reader.line()}")
//        reader.nextLine()
//    } while (reader.hasNext())

}