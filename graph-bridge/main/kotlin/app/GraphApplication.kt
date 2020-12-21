package app

import graph.DrawableGraph
import visualizer.Visualizer

abstract class GraphApplication(
    val areaWidth: Int,
    val areaHeight: Int,
    val graphBuilder: (Visualizer) -> DrawableGraph
) {

    abstract fun run()

}