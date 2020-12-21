package visualizer.impl

import drawArrow
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.scene.text.Text
import visualizer.ActionType
import visualizer.Visualizer
import visualizer.data.Point

class JFXVisualizer(
    private val canvas: Canvas,
    areaWidth: Int,
    areaHeight: Int
) : Visualizer(areaWidth, areaHeight) {

    private val gc = canvas.graphicsContext2D
    private val NO_TRANSFORM = gc.transform

    override fun drawText(position: Point, text: String) {
        val boundingBox = Text(text)
        val x = position.x - boundingBox.boundsInLocal.width / 2
        val y = position.y + boundingBox.boundsInLocal.height / 3
        actions.add(ActionType.Text to {
            gc.fill = Color.RED
            gc.fillText(text, x, y)
            gc.fill = Color.BLACK
        })
    }

    override fun drawCircle(center: Point, radius: Double) {
        actions.add(ActionType.Circle to {
            gc.fillOval(
                center.x - radius, center.y - radius,
                2 * radius, 2 * radius
            )
        })
    }

    override fun drawLine(start: Point, end: Point) {
        actions.add(ActionType.Line to {
            gc.strokeLine(start.x, start.y, end.x, end.y)
        })
        actions.add(ActionType.Arrow to {
            gc.stroke = Color.BLUE
            drawArrow(start, end, gc::strokeLine)
            gc.stroke = Color.BLACK
        })
    }

    override fun clear() {
        super.clear()
        gc.transform = NO_TRANSFORM
        gc.stroke = Color.BLACK
        gc.fill = Color.BLACK
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        gc.translate(areaWidth.toDouble() / 2, areaHeight.toDouble() / 2)
    }

}