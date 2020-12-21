package graph

abstract class DrawableGraph {

    protected abstract fun drawVertex(v: Int)
    protected abstract fun drawEdge(s: Int, t: Int)

    abstract fun drawGraph()

}