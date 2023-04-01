package com.example.todoapp.features.addTask.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.features.addTask.ui.model.TaskModel
import java.util.UUID
import javax.inject.Inject

class TasksViewModel @Inject constructor() : ViewModel() {
    private val _showAddDialog = MutableLiveData<Boolean>()
    val showAddDialog: LiveData<Boolean> = _showAddDialog

    private val _showDeleteDialog = MutableLiveData<Boolean>()
    val showDeleteDialog: LiveData<Boolean> = _showDeleteDialog

    private val _taskList = mutableStateListOf<TaskModel>()
    val taskList: List<TaskModel> = _taskList

    private val _idTaskSelection = MutableLiveData<UUID>()

    fun onAddDialogClose() {
        _showAddDialog.value = false
    }

    fun onAddDialogOpen() {
        _showAddDialog.value = true
    }

    fun onDeleteADialogClose() {
        _showDeleteDialog.value = false
    }

    fun onPressTask(task: TaskModel){
        _idTaskSelection.value = task.id
        _showDeleteDialog.value = true
    }

    fun onTaskAdded(newTask: String) {
        _taskList.add(TaskModel(name = newTask))
        _showAddDialog.value = false
    }

    fun onSelectedTask(task: TaskModel){
        val taskIndex = _taskList.indexOf(task)
        _taskList[taskIndex] = _taskList[taskIndex].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onDeletedTask(){
        val taskFound = _taskList.find { it.id == _idTaskSelection.value }
        _taskList.remove(taskFound);
        _showDeleteDialog.value = false
    }

    fun onDeleteAllTask(){
        _taskList.clear()
    }
}