package graph.impl

import graph.GraphCore
import visualizer.Visualizer
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.sqrt

class AdjacencyMatrixGraph(
    private val matrix: Array<BooleanArray>,
    visualizer: Visualizer
) : GraphCore(matrix.size, visualizer) {

    init {
        assert(matrix.isNotEmpty()) { "Adjacency matrix can not be empty" }
        assert(matrix.size == matrix[0].size) { "Adjacency matrix should be square" }
    }

    constructor(size: Int, visualizer: Visualizer) : this(
        Array(size) { BooleanArray(size) { false } },
        visualizer
    )

    override fun vertex(s: Int) = object : VertexAccessor {
        override fun connectTo(t: Int) {
            matrix[s][t] = true
        }

        override fun disconnectFrom(t: Int) {
            matrix[s][t] = false
        }

        override val neighbors: List<Int>
            get() = matrix[s].mapIndexed { t, connected ->
                if (connected) t else null
            }.filterNotNull()
    }

    override val edges: Iterable<Pair<Int, Int>>
        get() = vertices.flatMap { s ->
            vertex(s).neighbors.map { t -> s to t }
        }

    override fun vertexRadius(v: Int): Double {
        return super.vertexRadius(v) * (1.0 + max(
            0.0,
            log2(vertex(v).neighbors.size / sqrt(size.toDouble()))
        ))
    }

}