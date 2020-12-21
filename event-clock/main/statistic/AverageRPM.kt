package statistic

import clock.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class AverageRPM(private val clock: Clock) : EventStatistic<Double> {

    private val startTime = clock()
    private val statistic = ConcurrentHashMap<String, Long>()
    private val eventQueue = ConcurrentLinkedQueue<Pair<String, Instant>>()

    private val lock = ReentrantReadWriteLock()

    private val threshold
        get() = clock() - Duration.ofHours(1)
    private val minutesPassed
        get() = minOf(Duration.ofHours(1), Duration.between(startTime, clock())).toMinutes()

    override fun incrementEvent(eventName: String) {
        eventQueue.add(eventName to clock())
        statistic.compute(eventName) { _, count -> (count ?: 0) + 1 }
    }

    private fun cleanupRequired(timestamp: Instant): Boolean = lock.read {
        eventQueue.isNotEmpty() && eventQueue.first().second < timestamp
    }

    private fun cleanup(timestamp: Instant) = lock.write {
        while (eventQueue.isNotEmpty() && eventQueue.first().second < timestamp) {
            statistic.compute(eventQueue.remove().first) { _, count ->
                val newCount = count!! - 1
                if (newCount > 0) newCount else null
            }
        }
    }

    override fun getEventStatisticByName(eventName: String): Double = lock.read {
        val timestamp = threshold
        if (cleanupRequired(timestamp)) {
            cleanup(timestamp)
        }
        return (statistic.getOrDefault(eventName, 0L)).toDouble() / maxOf(minutesPassed, 1L)
    }

    override fun getAllEventStatistic(): Map<String, Double> = lock.read {
        val timestamp = threshold
        if (cleanupRequired(timestamp)) {
            cleanup(timestamp)
        }
        val minutes = maxOf(minutesPassed, 1L)
        return statistic.mapValues { (_, count) -> count.toDouble() / minutes }
    }

    override fun printStatistic() {
        val allStatistic = getAllEventStatistic()
        allStatistic.forEach { (name, rpm) ->
            println("Event $name: average rpm = $rpm")
        }
    }

}