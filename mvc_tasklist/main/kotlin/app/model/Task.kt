package app.model

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    var status: TaskStatus
)