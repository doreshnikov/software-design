package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import kotlin.random.Random

internal object TimeTransformersUnit {

    @Test
    fun testToSeconds() {
        repeat(10) {
            val seconds = Random.nextLong(1L, 2L shl 30)
            assertEquals(Instant.ofEpochSecond(seconds).toSeconds(), seconds)
        }
    }

    @Test
    fun testTimestamps() {
        assertThrows(IllegalArgumentException::class.java) {
            Instant.now().timestamps(0)
        }
        repeat(10) {
            val seconds = Random.nextLong(2L shl 19, 2L shl 20)
            val time = Instant.ofEpochSecond(seconds)

            val hours = Random.nextInt(1, 24)
            val timestamps = time.timestamps(hours.toLong())

            assertEquals(timestamps.size, hours)
            assertEquals(timestamps[hours - 1].second, time)
            (1 until timestamps.size).forEach { h ->
                assertEquals(timestamps[h].first + Duration.ofHours(1), timestamps[h].second)
                assertEquals(timestamps[h - 1].second, timestamps[h].first)
            }
        }
    }

}