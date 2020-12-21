package clock

import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class ExtendedClock(
    initialTime: Instant = Instant.now()
) : Clock {

    private var localTime = initialTime
    private var startedAt: Instant? = null
    private val running
        get() = startedAt != null

    private val lock = ReentrantReadWriteLock()

    override fun now(): Instant = lock.read {
        val startedTime = startedAt ?: return@read localTime
        localTime + Duration.between(startedTime, Instant.now())
    }

    fun start() = lock.write {
        if (!running) {
            startedAt = Instant.now()
        }
    }

    fun stop() = lock.write {
        if (running) {
            localTime = now()
            startedAt = null
        }
    }

    fun set(time: Instant) = lock.write {
        startedAt = null
        localTime = time
    }

    operator fun plusAssign(duration: TemporalAmount) = lock.write {
        localTime += duration
    }

    operator fun minusAssign(duration: TemporalAmount) = lock.write {
        localTime -= duration
    }

}