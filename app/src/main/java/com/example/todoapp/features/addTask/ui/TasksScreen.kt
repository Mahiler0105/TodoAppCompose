package com.example.todoapp.features.addTask.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.todoapp.features.addTask.ui.model.TaskModel

@Composable
fun TaskScreen(tasksViewModel: TasksViewModel) {
    val showDialog: Boolean by tasksViewModel.showAddDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by tasksViewModel.showDeleteDialog.observeAsState(initial = false)

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    val uiState by produceState<TaskUiState>(
        initialValue = TaskUiState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel)
        {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED){
                tasksViewModel.uiState.collect{ value = it }
            }
        }

    when(uiState){
        is TaskUiState.Error -> {
            Toast.makeText(context, "Ocurrió un error", Toast.LENGTH_LONG).show()
        }
        TaskUiState.Loading -> {
            CircularProgressIndicator()
        }
        is TaskUiState.Success -> {
            Column(Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = "App Todo")}, actions = {
                         if((uiState as TaskUiState.Success).tasks.isNotEmpty()){
                             Row() {
                                 Icon(imageVector = Icons.Default.Delete, contentDescription = "delete_all",
                                     modifier = Modifier.clickable {
                                         tasksViewModel.onDeleteAllTask()
                                     })
                             }
                         }
                    })},
                    floatingActionButton = {FabAddTask(tasksViewModel)}
                ){
                    Box(modifier = Modifier.padding(it))
                    TaskList((uiState as TaskUiState.Success).tasks, tasksViewModel)
                    NewTaskDialog(
                        showDialog,
                        onDismiss = { tasksViewModel.onAddDialogClose() },
                        onTaskAdded = { newTask -> tasksViewModel.onTaskAdded(newTask) })
                    DeleteTaskDialog(
                        showDeleteDialog,
                        onDismiss = { tasksViewModel.onDeleteADialogClose() },
                        onDeleted = { tasksViewModel.onDeletedTask() }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskList(tasks: List<TaskModel>, tasksViewModel: TasksViewModel){
    if(tasks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = "No tienes tareas creadas")
        }
    } else {
        LazyColumn{
            items(tasks,  key = { it.id }){
                ItemTask(it, tasksViewModel)
            }
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, tasksViewModel: TasksViewModel){
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        tasksViewModel.onPressTask(taskModel)
                    }
                )
            }, elevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = taskModel.name)
            Checkbox(checked = taskModel.selected, onCheckedChange = { tasksViewModel.onSelectedTask(taskModel) })
        }
    }
}

@Composable
fun FabAddTask(tasksViewModel: TasksViewModel) {
    FloatingActionButton(onClick = { tasksViewModel.onAddDialogOpen() }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "add_task")
    }
}

@Composable
fun DeleteTaskDialog(show: Boolean, onDismiss: () -> Unit, onDeleted: () -> Unit){
    if(show){
        AlertDialog(
            title = {
                Text(text = "Seguro que desea eliminar la tarea")
            },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onDeleted() }) {
                    Text(text = "Eliminar tarea")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}

@Composable
fun NewTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit){
    var newTask by rememberSaveable { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(size = 10.dp)
            ) {
                Column(Modifier.padding(horizontal = 15.dp, vertical = 15.dp)) {
                    Text(text = "Añade una tarea", Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.size(10.dp))
                    TextField(value = newTask, onValueChange = { newTask = it }, Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(onClick = { onTaskAdded(newTask); newTask = "" }, Modifier.fillMaxWidth(), enabled = newTask.isNotEmpty()) {
                        Text(text = "Añadir")
                    }
                }
            }
        }
    }
}
