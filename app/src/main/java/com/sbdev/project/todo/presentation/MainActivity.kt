@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.sbdev.project.todo.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbdev.project.todo.R
import com.sbdev.project.todo.presentation.components.AddToDoScreen
import com.sbdev.project.todo.presentation.components.FilterGroupView
import com.sbdev.project.todo.presentation.components.HeaderView
import com.sbdev.project.todo.presentation.components.ToDoItemView
import com.sbdev.project.todo.data.FilterItem
import com.sbdev.project.todo.data.ToDoItem
import com.sbdev.project.todo.presentation.viewmodels.AuthViewModel
import com.sbdev.project.todo.presentation.viewmodels.FirestoreViewModel
import com.sbdev.project.todo.ui.theme.ToDoTheme
import com.sbdev.project.todo.utils.getFilterList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val firestoreViewModel: FirestoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val toDoList = firestoreViewModel.toDoList.observeAsState()

                    LaunchedEffect(key1 = Unit) {
                        firestoreViewModel.getAllToDos()
                    }

                    MainScreen(
                        accountDp = authViewModel.currentUser?.photoUrl?.toString(),
                        list = toDoList,
                        onLogOut = {
                            authViewModel.signOut()

                            Intent(this, SignInActivity::class.java).also {
                                startActivity(it)
                                finish()
                            }
                        },
                        addToDo = { toDoItem ->
                            if (toDoItem.id.isEmpty()) {
                                firestoreViewModel.addToDo(toDoItem)
                            } else {
                                firestoreViewModel.updateToDo(toDoItem)
                            }
                        },
                        deleteToDo = { toDoItem ->
                            firestoreViewModel.deleteToDo(toDoItem)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreen(
    accountDp: String? = null,
    onLogOut: () -> Unit = {},
    list: State<List<ToDoItem>?> = mutableStateOf(emptyList()),
    addToDo: (ToDoItem) -> Unit = {},
    deleteToDo: (ToDoItem) -> Unit = {}
) {
    val selectedFilter = remember {
        mutableStateOf(FilterItem("All"))
    }

    var searchQuery by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedToDo by remember {
        mutableStateOf(ToDoItem())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
//                modifier = Modifier.padding(16.dp),
                onClick = {
                    selectedToDo = ToDoItem()
                    showBottomSheet = true
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_add_task),
                    contentDescription = "add to-do"
                )
            }
        }
    ) { contentPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                AddToDoScreen(
                    toDoItem = selectedToDo,
                    onSave = { toDoItem ->
                        addToDo.invoke(toDoItem)
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )
            }
        }

        Column {
            HeaderView(
                accountDp = accountDp,
                searchText = searchQuery,
                onSearchChanged = {
                    searchQuery = it
                },
                onLogOut = {
                    onLogOut.invoke()
                }
            )

            FilterGroupView(
                list = getFilterList(),
                selectedFilter = selectedFilter.value,
                onSelectedChanged = {
                    selectedFilter.value = it
                }
            )
            ToDoList(
                modifier = Modifier.weight(1f),
                list = if (selectedFilter.value.status == null)
                    list.value?.filter {
                        it.title.contains(searchQuery) || it.description.contains(
                            searchQuery
                        )
                    } ?: emptyList()
                else
                    list.value
                        ?.filter { it.status == selectedFilter.value.status!!.value }
                        ?.filter {
                            it.title.contains(searchQuery) || it.description.contains(searchQuery)
                        }
                        ?: emptyList(),
//                list = getToDoItems(selectedFilter.value.status)
                onEdit = {
                    selectedToDo = it
                    showBottomSheet = true
                },
                onDelete = {
                    deleteToDo.invoke(it)
                }
            )
        }
    }

}

@Composable
fun ToDoList(
    modifier: Modifier = Modifier,
    list: List<ToDoItem>,
    onEdit: (ToDoItem) -> Unit,
    onDelete: (ToDoItem) -> Unit
) {
    if (list.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.to_do),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No task on your list",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(list) {
                ToDoItemView(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth(),
                    item = it,
                    onEdit = { onEdit.invoke(it) },
                    onDelete = { onDelete.invoke(it) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

}