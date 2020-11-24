package dao

import model.Task
import model.TaskList
import model.TaskStatus

interface TaskListRepository {

    fun selectAllTaskLists(): List<TaskList>
    fun selectTaskList(taskListId: Long): TaskList?
    fun insertTaskList(title: String)
    fun deleteTaskList(taskListId: Long)

    fun selectTask(taskId: Long): Task?
    fun insertTask(taskListId: Long, title: String, description: String)
    fun deleteTask(taskId: Long)
    fun updateTaskStatus(taskId: Long, taskStatus: TaskStatus)

}