package lru_cache

import org.junit.jupiter.api.Assertions.*

internal class LRUCacheTest {

    private val cache = LRUCache<Int, String>(capacity = 5)

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        listOf(
            1 to "a",
            2 to "b",
            3 to "c",
            4 to "d"
        ).forEach {
            cache[it.first] = it.second
        }
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        cache.clear()
    }

    @org.junit.jupiter.api.Test
    fun testLRU() {
        assertEquals(cache[1], "a")
        assertDoesNotThrow {
            cache[5] = "e"
            cache[6] = "f"
        }
        assertNull(cache[2])
        assertEquals(cache.getOrDefault(2) { "z" }, "z")
        assertDoesNotThrow {
            cache[3]
            cache[7] = "g"
            assertEquals(cache.remove(5), "e")
            cache[8] = "h"
        }
        assertNull(cache[4])
        assertEquals(cache.getOrSet(4) { "z" }, "z")
        assertNull(cache[1])
        assertEquals(cache[4], "z")
    }

    @org.junit.jupiter.api.Test
    fun testZeroCapacity() {
        cache.capacity = 0
        assertEquals(cache.size, 0)
        assertDoesNotThrow {
            cache[10] = "a"
            assertNull(cache[10])
            assertEquals(cache.getOrSet(20) { "b" }, "b")
            assertNull(cache[20])
        }
    }

    @org.junit.jupiter.api.Test
    fun getCapacity() {
        assertEquals(cache.capacity, 5)
    }

    @org.junit.jupiter.api.Test
    fun setCapacity() {
        assertThrows(IllegalArgumentException::class.java) {
            cache.capacity = -1
        }
        assertDoesNotThrow {
            cache.capacity = 3
        }
        assertEquals(cache.size, 3)
        assertNull(cache[1])
    }

    @org.junit.jupiter.api.Test
    fun getSize() {
        assertEquals(cache.size, 4)
    }

    @org.junit.jupiter.api.Test
    fun isEmpty() {
        assertEquals(cache.isEmpty(), false)
        cache.clear()
        assertEquals(cache.isEmpty(), true)
    }

    @org.junit.jupiter.api.Test
    fun set() {
        assertDoesNotThrow {
            cache[1] = "z"
            cache[4] = "y"
            cache[5] = "x"
        }
        assertEquals(cache[1], "z")
        assertEquals(cache[4], "y")
        assertEquals(cache[5], "x")
    }

    @org.junit.jupiter.api.Test
    fun get() {
        assertEquals(cache[1], "a")
        assertEquals(cache[4], "d")
        assertNull(cache[5])
    }

    @org.junit.jupiter.api.Test
    fun getOrDefault() {
        assertEquals(cache.getOrDefault(1) { "z" }, "a")
        assertEquals(cache.getOrDefault(4) { "z" }, "d")
        assertEquals(cache.getOrDefault(5) { "z" }, "z")
        assertNull(cache[5])
    }

    @org.junit.jupiter.api.Test
    fun getOrSet() {
        assertEquals(cache.getOrSet(1) { "z" }, "a")
        assertEquals(cache.getOrSet(4) { "z" }, "d")
        assertEquals(cache.getOrSet(5) { "z" }, "z")
        assertEquals(cache[5], "z")
    }

    @org.junit.jupiter.api.Test
    fun remove() {
        assertNull(cache.remove(5))
        assertEquals(cache.remove(1), "a")
        assertNull(cache.remove(1))
    }

    @org.junit.jupiter.api.Test
    fun clear() {
        assertDoesNotThrow {
            cache.clear()
        }
        assertEquals(cache.size, 0)
        (1..4).forEach { assertNull(cache[it]) }
    }

}