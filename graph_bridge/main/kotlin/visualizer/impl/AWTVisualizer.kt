package visualizer.impl

import drawArrow
import visualizer.ActionType
import visualizer.Visualizer
import visualizer.data.Point
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D

class AWTVisualizer(
    private val g2: Graphics2D,
    areaWidth: Int,
    areaHeight: Int
) : Visualizer(areaWidth, areaHeight) {

    private val NO_TRANSFORM = g2.transform

    override fun drawText(position: Point, text: String) {
        val x = position.x - g2.fontMetrics.stringWidth(text) / 2
        val y = position.y + g2.fontMetrics.height / 3
        actions.add(ActionType.Text to {
            g2.color = Color.RED
            g2.drawString(text, x.toFloat(), y.toFloat())
            g2.color = Color.BLACK
        })
    }

    override fun drawCircle(center: Point, radius: Double) {
        actions.add(ActionType.Circle to {
            g2.fill(
                Ellipse2D.Double(
                    center.x - radius, center.y - radius,
                    2 * radius, 2 * radius
                )
            )
        })
    }

    override fun drawLine(start: Point, end: Point) {
        actions.add(ActionType.Line to {
            g2.draw(Line2D.Double(start.x, start.y, end.x, end.y))
        })
        actions.add(ActionType.Arrow to {
            g2.color = Color.BLUE
            drawArrow(start, end) { x1, y1, x2, y2 -> g2.draw(Line2D.Double(x1, y1, x2, y2)) }
            g2.color = Color.BLACK
        })
    }

    override fun clear() {
        super.clear()
        g2.transform = NO_TRANSFORM
        g2.stroke = BasicStroke(1.0F)
        g2.clearRect(0, 0, areaWidth, areaHeight)
        g2.translate(areaWidth.toDouble() / 2, areaHeight.toDouble() / 2)
    }

}