import app.GraphApplication
import app.impl.AWTApplication
import app.impl.JFXApplication
import graph.DrawableGraph
import graph.GraphCore
import graph.impl.AdjacencyMatrixGraph
import graph.impl.EdgeListGraph
import visualizer.Visualizer
import kotlin.random.Random

fun GraphCore.fillRandom(averageDegree: Int) {
    vertices.forEach { s ->
        vertices.forEach { t ->
            if (s != t && Random.nextInt(size) < averageDegree) {
                vertex(s) connectTo t
            }
        }
    }
}

const val WIDTH = 1200
const val HEIGHT = 720

fun main(args: Array<String>) {

    require(args.size in 4..5) { "Expected at least <size>, <avg>, <graph> and <visualizer>" }
    val size = args[0].toInt()
    val averageDegree = args[1].toInt()

    val graphBuilder: (Visualizer) -> DrawableGraph = when (args[2]) {
        "-list" -> fun(visualizer: Visualizer): DrawableGraph {
            return EdgeListGraph(size, visualizer).also { it.fillRandom(averageDegree) }
        }
        "-matrix" -> fun (visualizer: Visualizer): DrawableGraph {
            return AdjacencyMatrixGraph(size, visualizer).also { it.fillRandom(averageDegree) }
        }
        else -> error("Invalid graph type, expected '-list' or '-matrix'")
    }
    val application: GraphApplication = when (args[3]) {
        "-awt" -> AWTApplication(WIDTH, HEIGHT, graphBuilder)
        "-jfx" -> JFXApplication(WIDTH, HEIGHT, graphBuilder)
        else -> error("Invalid visualizer type, expected '-awt' or '-jfx'")
    }

    application.run()

}