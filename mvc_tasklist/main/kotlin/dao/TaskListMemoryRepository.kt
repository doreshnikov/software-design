package dao

import model.Task
import model.TaskList
import model.TaskStatus
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Component
object TaskListMemoryRepository : TaskListRepository {

    private val nextTaskListId = AtomicLong(0)
    private val nextTaskId = AtomicLong(0)

    private val storage = ConcurrentHashMap<Long, TaskList>()
    private val tasksMapping = ConcurrentHashMap<Long, Long>()

    override fun selectAllTaskLists(): List<TaskList> =
        storage.values.sortedBy { it.id }

    override fun selectTaskList(taskListId: Long): TaskList? =
        storage[taskListId]

    override fun insertTaskList(title: String) {
        val taskListId = nextTaskListId.incrementAndGet()
        storage[taskListId] = TaskList(taskListId, title)
    }

    override fun deleteTaskList(taskListId: Long) {
        storage.remove(taskListId)
    }

    override fun selectTask(taskId: Long): Task? =
        storage[tasksMapping[taskId] ?: 0]
            ?.tasks
            ?.first { it.id == taskId }

    override fun insertTask(taskListId: Long, title: String, description: String) {
        storage[taskListId]?.let {
            val taskId = nextTaskId.incrementAndGet()
            it.add(Task(taskId, title, description, TaskStatus.Opened))
            tasksMapping[taskId] = taskListId
        }
    }

    override fun deleteTask(taskId: Long) {
        storage[tasksMapping[taskId] ?: 0]?.let {
            it.removeIf { task -> task.id == taskId }
            tasksMapping.remove(taskId)
        }
    }

    override fun updateTaskStatus(taskId: Long, taskStatus: TaskStatus) {
        selectTask(taskId)?.status = taskStatus
    }

}