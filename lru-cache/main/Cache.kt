package lru_cache

interface Cache<K, V> {

    fun isEmpty(): Boolean

    operator fun set(key: K, value: V)

    operator fun get(key: K): V?

    fun getOrDefault(key: K, defaultValue: () -> V): V

    fun getOrSet(key: K, defaultValue: () -> V): V

    fun remove(key: K): V?

    fun clear()

}