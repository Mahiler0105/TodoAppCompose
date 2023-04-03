package com.example.todoapp.features.addTask.data

import com.example.todoapp.features.addTask.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val tasks: Flow<List<TaskModel>> =
        taskDao.getTasks().map {
            it.map { taskEntity ->
                TaskModel(id = taskEntity.id, name = taskEntity.name, selected = taskEntity.selected)
            }
    }
    suspend fun add(task: TaskModel){
        taskDao.addTask(TaskEntity(id = task.id, name = task.name, selected = task.selected))
    }

    suspend fun update(task: TaskModel){
        taskDao.updateTask(TaskEntity(id = task.id, name = task.name, selected = task.selected))
    }

    suspend fun delete(task: TaskModel){
        taskDao.deleteTask(TaskEntity(id = task.id, name = task.name, selected = task.selected))
    }

    suspend fun deleteAll(){
        taskDao.deleteAllTasks()
    }
}