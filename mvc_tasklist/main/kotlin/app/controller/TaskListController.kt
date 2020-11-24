package app.controller

import app.dao.TaskListRepository
import app.model.TaskStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TaskListController(
    @Autowired private val repository: TaskListRepository
) {

    @GetMapping("/task_lists")
    fun getTaskLists(model: Model): String {
        model.addAttribute("task_lists", repository.selectAllTaskLists())
        return "index"
    }

    @GetMapping("/task_list")
    fun getTaskList(model: Model, @RequestParam taskListId: Long): String {
        model.addAttribute("task_lists", listOf(repository.selectTaskList(taskListId)))
        return "index"
    }

    @PostMapping("/task_list_put")
    fun putTaskList(@RequestParam title: String): String {
        repository.insertTaskList(title)
        return "redirect:/task_lists"
    }

    @PostMapping("/task_list_delete")
    fun deleteTaskList(@RequestParam taskListId: Long): String {
        repository.deleteTaskList(taskListId)
        return "redirect:/task_lists"
    }

    @GetMapping("/task")
    @Deprecated("Not implemented since no there are no use cases")
    fun getTask() {
    }

    @PostMapping("/task_put")
    fun putTask(
        @RequestParam taskListId: Long,
        @RequestParam title: String,
        @RequestParam description: String
    ): String {
        repository.insertTask(taskListId, title, description)
        return "redirect:/task_lists"
    }

    @PostMapping("/task_delete")
    fun deleteTask(@RequestParam taskId: Long): String {
        repository.deleteTask(taskId)
        return "redirect:/task_lists"
    }

    @PostMapping("/task")
    fun updateTaskStatus(@RequestParam taskId: Long, @RequestParam taskStatus: String): String {
        val status = TaskStatus.valueOf(taskStatus)
        repository.updateTaskStatus(taskId, status)
        return "redirect:/task_lists"
    }

}