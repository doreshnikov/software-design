import visualizer.Visualizer
import visualizer.data.Point
import kotlin.math.PI
import kotlin.math.atan2

fun drawArrow(start: Point, end: Point, drawLine: (Double, Double, Double, Double) -> Unit) {
    val angle = atan2(end.y - start.y, end.x - start.x)
    val middle = (start * 3.0 + end) * 0.25
    val p1 = middle + Point(angle + PI * 3 / 4) * Visualizer.ARROW_LENGTH
    val p2 = middle + Point(angle - PI * 3 / 4) * Visualizer.ARROW_LENGTH
    drawLine(middle.x, middle.y, p1.x, p1.y)
    drawLine(middle.x, middle.y, p2.x, p2.y)
}