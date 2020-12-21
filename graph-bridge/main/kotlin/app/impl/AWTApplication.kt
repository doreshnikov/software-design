package app.impl

import app.GraphApplication
import graph.DrawableGraph
import visualizer.impl.AWTVisualizer
import visualizer.Visualizer
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class AWTApplication(
    areaWidth: Int,
    areaHeight: Int,
    graphBuilder: (Visualizer) -> DrawableGraph
) : GraphApplication(areaWidth, areaHeight, graphBuilder) {

    class AWTFrame(
        private val areaWidth: Int,
        private val areaHeight: Int,
        val graphBuilder: (AWTVisualizer) -> DrawableGraph
    ): Frame() {

        init {
            addWindowListener(CloseListener)
            setSize(areaWidth, areaHeight)
            isResizable = false
        }

        companion object {
            object CloseListener : WindowAdapter() {
                override fun windowClosing(e: WindowEvent?) {
                    exitProcess(0)
                }
            }
        }

        override fun paint(g: Graphics) {
            super.paint(g)
            val g2d = g as Graphics2D
            val visualizer = AWTVisualizer(g2d, areaWidth, areaHeight)
            graphBuilder(visualizer).drawGraph()
        }
    }

    override fun run() {
        AWTFrame(areaWidth, areaHeight, graphBuilder).isVisible = true
    }

}