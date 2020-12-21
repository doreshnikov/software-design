package utils

import java.time.Duration
import java.time.Instant

fun Instant.toSeconds(): Long {
    return epochSecond
}

fun Instant.beforeHours(hours: Long): Instant {
    return this - Duration.ofHours(hours)
}

fun Instant.timestamps(hoursBefore: Long): List<Pair<Instant, Instant>> {
    require(hoursBefore > 0) { "Expected non-zero time interval" }
    return (hoursBefore downTo 1).map { hours ->
        val start = this.beforeHours(hours)
        start to start + Duration.ofHours(1)
    }
}