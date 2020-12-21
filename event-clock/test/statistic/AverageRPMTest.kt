package statistic

import clock.ExtendedClock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.fail
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class AverageRPMTest {

    private lateinit var statistic: AverageRPM
    private lateinit var clock: ExtendedClock

    private fun runMany(n: Int, action: (Int) -> Unit) {
        val threads = mutableListOf<Thread>()
        repeat(n) { id ->
            threads.add(Thread {
                action(id)
            }.also { it.start() })
        }
        threads.forEach { it.join() }
    }

    private fun compare(expectedResult: Map<String, Double>) {
        val allStatistic = statistic.getAllEventStatistic()
        assertEquals(allStatistic.size, expectedResult.size) {
            "Expected statistics to have same amount of events"
        }
        expectedResult.forEach { (name, rpm) ->
            val stat = allStatistic[name] ?: fail { "Expected to have statistic for event '$name'" }
            assertTrue(kotlin.math.abs(stat - rpm) < 1e-6) {
                "Expected event '$name' to have rpm of $rpm"
            }
        }
    }

    @org.junit.jupiter.api.BeforeEach
    fun prepare() {
        clock = ExtendedClock()
        statistic = AverageRPM(clock)
    }

    @org.junit.jupiter.api.Test
    fun zeroOneMinutesTest() {
        runMany(5) { id ->
            for (i in 0..id) {
                statistic.incrementEvent("event$i")
            }
        }
        for (i in 0..1) {
            compare(
                mapOf(
                    "event0" to 5.0,
                    "event1" to 4.0,
                    "event2" to 3.0,
                    "event3" to 2.0,
                    "event4" to 1.0
                )
            )
            clock += Duration.ofMinutes(1)
        }
    }

    @org.junit.jupiter.api.Test
    fun missingEventTest() {
        statistic.incrementEvent("event1")
        clock += Duration.ofMinutes(10)
        compare(mapOf("event1" to 0.1))
        assertEquals(statistic.getEventStatisticByName("event2"), 0.0) {
            "Expected statistic for event2 to be 0.0"
        }
    }

    @org.junit.jupiter.api.Test
    fun oneHourSpamTest() {
        val count = ConcurrentHashMap<String, Long>()
        runMany(60) {
            for (event in 0..1) {
                for (i in 0..Random.nextInt(5)) {
                    statistic.incrementEvent("event$event")
                    count.compute("event$event") { _, c -> (c ?: 0) + 1 }
                }
            }
            clock += Duration.ofMinutes(1)
        }
        compare(count.mapValues { (_, c) -> c.toDouble() / 60 })
    }

    @org.junit.jupiter.api.Test
    fun moreThanHourForgetTest() {
        statistic.incrementEvent("event1")
        clock += Duration.ofSeconds(1)
        statistic.incrementEvent("event2")
        clock += Duration.ofSeconds(59)
        statistic.incrementEvent("event3")
        clock += Duration.ofMinutes(59)
        statistic.incrementEvent("event4")
        clock += Duration.ofMillis(1)
        compare(
            mapOf(
                "event2" to 1.0 / 60,
                "event3" to 1.0 / 60,
                "event4" to 1.0 / 60
            )
        )
    }

}