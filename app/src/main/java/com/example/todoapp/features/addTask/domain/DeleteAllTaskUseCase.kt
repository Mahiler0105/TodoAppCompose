package com.example.todoapp.features.addTask.domain

import com.example.todoapp.features.addTask.data.TaskRepository
import javax.inject.Inject

class DeleteAllTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(){
        taskRepository.deleteAll()
    }
}