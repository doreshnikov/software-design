package lru_cache.utils

abstract class DoublyLinkedSequence<T> {

    protected abstract class Node<T>(
        var previous: Node<T>? = null,
        var next: DataNode<T>? = null
    ) {
        open val isTailing: Boolean = false

        fun cut(checkValid: Boolean = false) {
            previous?.let {
                if (checkValid) assert(it.next === this)
                it.next = next
            }
            next?.let {
                if (checkValid) assert(it.previous === this)
                it.previous = previous
            }
            previous = null; next = null
        }
    }

    protected class DataNode<T>(
        var value: T,
        previous: Node<T>? = null,
        next: DataNode<T>? = null
    ) : Node<T>(previous, next) {
        infix fun insertedAfter(node: Node<T>): Node<T> {
            node.next?.let { it.previous = this }
            previous = node; next = node.next
            node.next = this
            return this
        }
    }

    protected abstract fun insert(node: DataNode<T>)

    protected abstract fun erase(node: DataNode<T>)

    abstract fun isEmpty(): Boolean

}