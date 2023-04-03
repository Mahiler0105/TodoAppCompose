package com.example.todoapp.features.addTask.domain

import com.example.todoapp.features.addTask.data.TaskRepository
import com.example.todoapp.features.addTask.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel){
        taskRepository.add(task)
    }
}