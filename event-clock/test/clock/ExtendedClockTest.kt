package clock

import java.time.Duration
import java.time.Instant

import org.junit.jupiter.api.Assertions.*

class ExtendedClockTest {

    private lateinit var clock: ExtendedClock
    private lateinit var timestamp: Instant

    @org.junit.jupiter.api.BeforeEach
    fun prepare() {
        clock = ExtendedClock()
        timestamp = clock()
    }

    @org.junit.jupiter.api.Test
    fun steadyClockTest() {
        clock += Duration.ofHours(2)
        clock -= Duration.ofMinutes(20)
        assertEquals(Duration.between(timestamp, clock()).toMinutes(), 100L) {
            "Expected 100 minutes to pass"
        }
    }

    @org.junit.jupiter.api.Test
    fun runningClockTest() {
        clock.start()
        Thread.sleep(1000)
        clock.stop()
        assertTrue(kotlin.math.abs(Duration.between(timestamp, clock()).toMillis() - 1000) < 50) {
            "Expected passed time to be close to real time"
        }
    }

    @org.junit.jupiter.api.Test
    fun modifyRunningClockTest() {
        clock.start()
        clock += Duration.ofMillis(1000)
        Thread.sleep(1000)
        clock.stop()
        assertTrue(kotlin.math.abs(Duration.between(timestamp, clock()).toMillis() - 2000) < 50) {
            "Expected passed time to be close to real time"
        }
    }

    @org.junit.jupiter.api.Test
    fun setRunningClockTest() {
        clock.start()
        Thread.sleep(1000)
        clock.set(timestamp + Duration.ofMillis(5000))
        assertEquals(Duration.between(timestamp, clock()).toMillis(), 5000L) {
            "Expected passed time to be affected only by `set`"
        }
        Thread.sleep(1000)
        assertEquals(Duration.between(timestamp, clock()).toMillis(), 5000L) {
            "Expected clock to be stopped after `set`"
        }
    }

}