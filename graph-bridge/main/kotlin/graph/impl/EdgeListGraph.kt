package graph.impl

import graph.GraphCore
import visualizer.Visualizer
import java.util.*

class EdgeListGraph(
    size: Int,
    visualizer: Visualizer,
    val allowMultiEdges: Boolean = false
) : GraphCore(size, visualizer) {

    private val edgeList: MutableList<Pair<Int, Int>> = mutableListOf()

    override fun vertex(s: Int) = object : VertexAccessor {
        override fun connectTo(t: Int) {
            val edge = s to t
            if (!allowMultiEdges) {
                require(edge !in edgeList) { "Multiple edges are not allowed" }
            }
            edgeList.add(edge)
        }

        override fun disconnectFrom(t: Int) {
            require(edgeList.remove(s to t)) { "Edge is not present in the graph" }
        }

        override val neighbors: List<Int>
            get() = edgeList.filter { it.first == s }.map { it.second }
    }

    override val edges: Iterable<Pair<Int, Int>>
        get() = Collections.unmodifiableList(edgeList)

    override fun drawEdge(s: Int, t: Int) {
        super.drawEdge(s, t)
        if (allowMultiEdges) {
            val edgeCount = edgeList.count { it == s to t }
            visualizer.drawText(
                (vertexPosition(s) + vertexPosition(t)) * 0.5,
                "$edgeCount"
            )
        }
    }

}