package visualizer.data

import kotlin.math.cos
import kotlin.math.sin

data class Point(
    val x: Double,
    val y: Double
) {

    constructor(alpha: Double): this(cos(alpha), sin(alpha))

    operator fun times(t: Double) =
        Point(x * t, y * t)

    operator fun plus(p: Point) =
        Point(x + p.x, y + p.y)

}