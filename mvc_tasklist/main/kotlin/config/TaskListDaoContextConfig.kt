package config

import dao.TaskListMemoryRepository
import dao.TaskListRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TaskListDaoContextConfig {

    @Bean
    fun repository(): TaskListRepository = TaskListMemoryRepository

}