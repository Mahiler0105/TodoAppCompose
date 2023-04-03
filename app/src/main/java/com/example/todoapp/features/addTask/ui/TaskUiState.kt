package com.example.todoapp.features.addTask.ui

import com.example.todoapp.features.addTask.ui.model.TaskModel

sealed interface TaskUiState {
    object Loading: TaskUiState
    data class Error(val throwable: Throwable) : TaskUiState
    data class Success(val tasks: List<TaskModel>) : TaskUiState
}