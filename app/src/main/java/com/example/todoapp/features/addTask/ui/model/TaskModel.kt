package com.example.todoapp.features.addTask.ui.model

import java.util.UUID

data class TaskModel(val id: UUID = UUID.randomUUID(), val name: String, var selected: Boolean = false)
