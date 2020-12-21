package app.impl

import app.GraphApplication
import graph.DrawableGraph
import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import visualizer.Visualizer
import visualizer.impl.JFXVisualizer
import kotlin.properties.Delegates

class JFXApplication(
    areaWidth: Int,
    areaHeight: Int,
    graphBuilder: (Visualizer) -> DrawableGraph
) : GraphApplication(areaWidth, areaHeight, graphBuilder) {

    init {
        globalAreaWidth = areaWidth
        globalAreaHeight = areaHeight
        globalGraphBuilder = graphBuilder
    }

    companion object {
        private var globalAreaWidth by Delegates.notNull<Int>()
        private var globalAreaHeight by Delegates.notNull<Int>()
        private lateinit var globalGraphBuilder: (Visualizer) -> DrawableGraph
    }

    class JFXApp : Application() {
        override fun start(primaryStage: Stage) {
            val canvas = Canvas(globalAreaWidth.toDouble(), globalAreaHeight.toDouble())
            primaryStage.scene = Scene(Group(canvas), Color.WHITE)
            primaryStage.isResizable = false

            val visualizer = JFXVisualizer(canvas, globalAreaWidth, globalAreaHeight)
            globalGraphBuilder(visualizer).drawGraph()

            primaryStage.show()
        }
    }

    override fun run() {
        launch(JFXApp::class.java)
    }

}