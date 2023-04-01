package com.example.todoapp.features.addTask.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.features.addTask.ui.model.TaskModel

@Composable
fun TaskScreen(tasksViewModel: TasksViewModel) {
    val showDialog: Boolean by tasksViewModel.showAddDialog.observeAsState(initial = false)
    val showDeleteDialog: Boolean by tasksViewModel.showDeleteDialog.observeAsState(initial = false)

    Column(Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "App Todo")}, actions = {
                if(tasksViewModel.taskList.isNotEmpty()){
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
            TaskList(tasksViewModel)
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

@Composable
fun TaskList(viewModel: TasksViewModel){
    if(viewModel.taskList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = "No tienes tareas creadas")
        }
    } else {
        LazyColumn{
            items(viewModel.taskList, key = { it.id }){
                ItemTask(it, viewModel)
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
