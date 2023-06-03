package script

import java.awt.Color

val ACTOR_COLORS = setOf(
  Color(255, 204, 102),   // Peach
  Color(255, 255, 102),   // Canary Yellow
  Color(153, 255, 102),   // Lime Green
  Color(102, 255, 153),   // Mint Green
  Color(102, 255, 255),   // Light Blue
  Color(102, 153, 255),   // Sky Blue
  Color(153, 102, 255),   // Lavender
)



enum class Scene(val filePath: String) {
  Scene1("res/script_new_scene1.txt"),
  Scene2("res/script_new_scene2.txt"),
  Scene3("res/script_new_scene3.txt"),
  SceneAll("res/script_new.txt"),
}
object ScriptPrep {

  val actorsColorMap = mutableMapOf<String, Pair<Color, Color>>()

  // 0 - 255
  private val ALPHA = 128
  private val DEFAULT_COLOR = Pair(Color.WHITE, Color.WHITE)

  val actionColor = makeAlphaPair(Color(255, 102, 102))   // Light Coral
  val positionColor = makeAlphaPair(Color(160, 160, 160))   // Orchid

  fun fillActorsColorMap() {
    val scriptAll: Script = Script(Scene.SceneAll)
    var iterator = ACTOR_COLORS.iterator()
    scriptAll.actorsNameOnly.forEach {
      if (!iterator.hasNext()) {
        iterator = ACTOR_COLORS.iterator()
      }
      actorsColorMap[it] = makeAlphaPair(iterator.next())
    }

    for (entry in actorsColorMap) {
      println("actor : ${entry.key}, color ${entry.value}")
    }
    println("Number of actors : ${actorsColorMap.keys.size}")
  }

  private fun makeAlphaPair(original: Color): Pair<Color, Color> {
    val alphaColor = lightenColor(original, 0.3f)
    return Pair(original, alphaColor)
  }

  fun lightenColor(color: Color, factor: Float): Color {
    val hsb = FloatArray(3)
    Color.RGBtoHSB(color.red, color.green, color.blue, hsb)
    val saturation = hsb[1]
    val newSaturation = Math.max(0f, saturation - factor)
    return Color.getHSBColor(hsb[0], newSaturation, hsb[2])
  }

  fun getColors(line: Line): Pair<Color, Color> {
    return when (line.type) {
      LineType.DIALOG -> actorsColorMap[line.actorNameOnly] ?: DEFAULT_COLOR
      LineType.ACTION -> {
        actorsColorMap[line.actorNameOnly] ?: actionColor
      }
      LineType.POSITION -> positionColor
      LineType.ELSE -> DEFAULT_COLOR
    }
  }
}