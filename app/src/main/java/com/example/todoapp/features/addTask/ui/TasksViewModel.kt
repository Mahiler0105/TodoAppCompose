package com.example.todoapp.features.addTask.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.features.addTask.domain.*
import com.example.todoapp.features.addTask.ui.TaskUiState.Success
import com.example.todoapp.features.addTask.ui.TaskUiState.Error
import com.example.todoapp.features.addTask.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val deleteAllTaskUseCase: DeleteAllTaskUseCase,
) : ViewModel() {

    val uiState: StateFlow<TaskUiState> = getTasksUseCase()
                                            .map(::Success)
                                            .catch {
                                                Error(it)
                                            }
                                            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUiState.Loading)

    private val _showAddDialog = MutableLiveData<Boolean>()
    val showAddDialog: LiveData<Boolean> = _showAddDialog

    private val _showDeleteDialog = MutableLiveData<Boolean>()
    val showDeleteDialog: LiveData<Boolean> = _showDeleteDialog

    private val _taskSelection = MutableLiveData<TaskModel>()

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
        _taskSelection.value = task
        _showDeleteDialog.value = true
    }

    fun onTaskAdded(newTask: String) {
        viewModelScope.launch {
            addTaskUseCase(TaskModel(name = newTask))
            _showAddDialog.value = false
        }
    }

    fun onSelectedTask(task: TaskModel){
        viewModelScope.launch {
            updateTaskUseCase(task.copy(selected = !task.selected))
        }
    }

    fun onDeletedTask(){
        viewModelScope.launch {
            deleteTaskUseCase(_taskSelection.value!!)
            _showDeleteDialog.value = false
        }
    }

    fun onDeleteAllTask(){
        viewModelScope.launch {
            deleteAllTaskUseCase()
        }
    }
}