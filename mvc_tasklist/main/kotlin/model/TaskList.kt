package model

data class TaskList(
    val id: Long,
    val title: String,
    val tasks: MutableList<Task> = mutableListOf()
) : MutableList<Task> by tasks