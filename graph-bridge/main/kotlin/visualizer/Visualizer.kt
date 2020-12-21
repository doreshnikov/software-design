package visualizer

import visualizer.data.Point

abstract class Visualizer(
    val areaWidth: Int,
    val areaHeight: Int
) {

    companion object {
        const val ARROW_LENGTH = 10.0
    }

    protected val actions: MutableList<Pair<ActionType, () -> Unit>> = mutableListOf()

    abstract fun drawText(position: Point, text: String)
    abstract fun drawCircle(center: Point, radius: Double)
    abstract fun drawLine(start: Point, end: Point)

    open fun clear() {
        actions.clear()
    }
    open fun show() {
        actions.sortedBy { it.first }.forEach {
            it.second()
        }
    }

}