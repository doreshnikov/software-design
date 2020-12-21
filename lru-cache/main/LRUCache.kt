package lru_cache

import lru_cache.utils.DoublyLinkedSequence

class LRUCache<K, V>(capacity: Int = DEFAULT_CAPACITY) : DoublyLinkedSequence<Pair<K, V>>(), Cache<K, V> {

    companion object {
        const val DEFAULT_CAPACITY = 100000
    }

    var capacity = capacity
        set(value) {
            if (value < 0) throw IllegalArgumentException("Capacity should be non-negative")
            while (value < size) eraseOldestNode()
            field = value
        }

    var size = 0
        private set(value) {
            assert(value <= capacity) { "Size should not exceed cache capacity" }
            field = value
        }

    private val limiter = object : Node<Pair<K, V>>() {
        override val isTailing = true
    }
    private var head: Node<Pair<K, V>> = limiter
    private val tail = limiter

    private val cache = HashMap<K, DataNode<Pair<K, V>>>()

    override fun insert(node: DataNode<Pair<K, V>>) {
        head = node insertedAfter head
        size++
    }

    private fun assertNotEmpty() {
        assert(size > 0) { "Size should be positive" }
        assert(head !== tail) { "Sequence should not be empty" }
    }

    override fun erase(node: DataNode<Pair<K, V>>) {
        assertNotEmpty()
        if (node === head) {
            assert(head.previous != null) { "Head should always have a predecessor" }
            head = head.previous!!
        }
        node.cut(checkValid = true)
        size--
    }

    private fun moveToFront(node: DataNode<Pair<K, V>>) {
        erase(node)
        insert(node)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun set(key: K, value: V) {
        cache[key]?.let {
            it.value = key to value
            moveToFront(it)
        } ?: run {
            if (capacity > 0) {
                val node = DataNode(key to value)
                if (size == capacity) {
                    eraseOldestNode()
                }
                insert(node)
                cache[key] = node
            }
        }
        assert(size == cache.size) { "Cache should be held in linked sequence" }
    }

    override fun get(key: K): V? {
        val node = cache[key] ?: return null
        moveToFront(node)
        return node.value.second
    }

    override fun getOrDefault(key: K, defaultValue: () -> V): V {
        return get(key) ?: defaultValue()
    }

    override fun getOrSet(key: K, defaultValue: () -> V): V {
        return get(key) ?: defaultValue().also { set(key, it) }
    }

    override fun remove(key: K): V? {
        val node = cache[key] ?: return null
        erase(node)
        cache.remove(key)
        return node.value.second
    }

    override fun clear() {
        tail.next = null
        head = tail
        size = 0
        cache.clear()
    }

    private fun eraseOldestNode() {
        assert(tail.next != null) { "Sequence should have an element after tail" }
        remove(tail.next!!.value.first)
    }

}