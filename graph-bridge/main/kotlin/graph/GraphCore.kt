package graph

import visualizer.Visualizer
import visualizer.data.Point
import kotlin.math.PI
import kotlin.math.min

abstract class GraphCore(
    val size: Int,
    protected val visualizer: Visualizer
) : DrawableGraph() {

    interface VertexAccessor {
        infix fun connectTo(t: Int)
        infix fun disconnectFrom(t: Int)

        fun connectToAll(vararg t: Int) {
            t.forEach(this::connectTo)
        }

        val neighbors: List<Int>
    }

    abstract fun vertex(s: Int): VertexAccessor
    val vertices
        get() = 0 until size
    abstract val edges: Iterable<Pair<Int, Int>>

    open fun vertexPosition(v: Int): Point {
        val radius = min(visualizer.areaWidth, visualizer.areaHeight).toDouble() / 3
        return Point(2 * PI / size * v) * radius
    }

    open fun vertexRadius(v: Int): Double {
        return min(visualizer.areaWidth, visualizer.areaHeight).toDouble() / size / 5
    }

    override fun drawVertex(v: Int) {
        val position = vertexPosition(v)
        visualizer.drawCircle(position, vertexRadius(v))
        visualizer.drawText(position, "$v")
    }

    override fun drawEdge(s: Int, t: Int) {
        visualizer.drawLine(vertexPosition(s), vertexPosition(t))
    }

    override fun drawGraph() {
        visualizer.clear()
        vertices.forEach { v ->
            drawVertex(v)
        }
        vertices.forEach { s ->
            vertex(s).neighbors.forEach { t -> drawEdge(s, t) }
        }
        visualizer.show()
    }

}