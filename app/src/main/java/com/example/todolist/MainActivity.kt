package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TodoScreen()
                }
            }
        }
    }
}

class TodoViewModel : ViewModel() {
    val todoItems: MutableState<List<TodoItem>> = mutableStateOf(listOf())

    fun addItem(item: TodoItem) {
        todoItems.value = todoItems.value + listOf(item)
    }

    fun removeItem(item: TodoItem) {
        todoItems.value = todoItems.value - listOf(item)
    }

    fun toggleItem(item: TodoItem) {
        val newList = todoItems.value.map {
            if (it == item) it.copy(isDone = !it.isDone) else it
        }
        todoItems.value = newList
    }
}

@Composable
fun TodoScreen(todoViewModel: TodoViewModel = viewModel()) {
    val todoItems = todoViewModel.todoItems.value

    Column {
        AddTodoItemInput(onItemComplete = { todoViewModel.addItem(TodoItem(it)) })
        TodoItemsList(todoItems, onItemClicked = {
            if (it.isDone) {
                todoViewModel.removeItem(it)
            } else {
                todoViewModel.toggleItem(it)
            }
        })
    }
}

@Composable
fun AddTodoItemInput(onItemComplete: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    Column {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Todo List") },
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Button(
                onClick = {
                    onItemComplete(input)
                    input = ""
                },
                modifier = Modifier.padding(start = 8.dp, top = 3.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent,
                )
            )
            {
                Text("+", fontSize = 30.sp, color = Color.Black)
            }

        }
    }
}

@Composable
fun TodoItemsList(items: List<TodoItem>, onItemClicked: (TodoItem) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(items) { item ->
            TodoItemRow(item, onItemClicked)
        }
    }
}

@Composable
fun TodoItemRow(item: TodoItem, onItemClicked: (TodoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(item.task)
        if (item.isDone) {
            Icon(Icons.Default.Check, contentDescription = "Check")
        }
    }
}
