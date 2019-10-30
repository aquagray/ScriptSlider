import java.io.File
import java.util.*
import kotlin.collections.ArrayList


val DEBUG_MODE = false

val SKIP = listOf("[music starts]", "<instrumental>", "[song starts]", "[song begins]")
class ScriptReader {
    private val scriptList = ArrayList<String>()
    private var curr = 0
    private val actors = HashSet<String>()

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
}

// Test script reader.
fun main(args: Array<String>) {
    val reader = ScriptReader()
    reader.load("res/script.txt")

//    reader.debug()

    do {
        println("Actor(${reader.actor()}) : ${reader.line()}")
        reader.nextLine()
    } while (reader.hasNext())

}