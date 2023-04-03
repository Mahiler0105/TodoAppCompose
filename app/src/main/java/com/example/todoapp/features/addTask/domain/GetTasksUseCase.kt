package com.example.todoapp.features.addTask.domain

import com.example.todoapp.features.addTask.data.TaskRepository
import com.example.todoapp.features.addTask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke() : Flow<List<TaskModel>> = taskRepository.tasks
}