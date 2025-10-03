package com.example.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun todoscreen(navController: NavHostController) {

    val context = LocalContext.current
    var showDialogue by remember { mutableStateOf(false) }
    val tasks = remember { mutableStateListOf<Pair<String, String>>() } // store title + content




    LaunchedEffect(Unit) {
        tasks.addAll(PrefsManager.loadTasks(context))
    }

    LaunchedEffect(tasks) {
        PrefsManager.saveTasks(context , tasks)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notes") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialogue = true },
                containerColor = colorResource(id= R.color.liteblue)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tasks.isEmpty()) {
                Text("Your tasks will appear here")
            } else {
                // Show tasks in a LazyColumn
                LazyColumn {
                    items(tasks.size) { index ->
                        val task = tasks[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp).
                            clickable{

                                navController.navigate("NT/${task.first}/${task.second}")


                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.first,
                                        style = MaterialTheme.typography.titleLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = task.second,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Dustbin icon for delete
                                IconButton(onClick = { tasks.removeAt(index)
                                    PrefsManager.saveTasks(context, tasks)}) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Task",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Show dialog
        if (showDialogue) {
            AddtaskDialogue(
                onDismiss = { showDialogue = false },
                onSave = { title, content ->
                    tasks.add(title to content)
                    PrefsManager.saveTasks(context, tasks)
                    showDialogue = false
                }
            )
        }
    }
}

@Composable
fun AddtaskDialogue(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title, content) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
