package app.model

enum class TaskStatus {
    Opened,
    InProgress,
    Closed;

    val next get() = values().let {
        it[(it.indexOf(this) + 1) % it.size]
    }
}