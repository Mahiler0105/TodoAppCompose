package com.example.todoapp.features.addTask.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TaskEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    var selected: Boolean = false)
