@file:OptIn(ExperimentalWearMaterialApi::class, ExperimentalMaterial3Api::class)

package com.example.noteshare.android.notes.list

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.placeholder
import androidx.wear.compose.material.rememberPlaceholderState
import com.example.noteshare.android.NoteRoute
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.list.presentation.NoteListIntent
import com.example.noteshare.notes.list.presentation.NoteListViewModel
import androidx.compose.runtime.DisposableEffect

@Composable
fun NotesScreen(viewModel: NoteListViewModel, router: NavHostController) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.cancelScope()
        }
    }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    "Your notes",
                    style = MaterialTheme.typography.displaySmall,
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        router.navigate(NoteRoute.AddNote.name)
                    },
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(5.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Note"
                    )
                }
            }
        },
    ) { paddingValues ->
        ContentView(modifier = Modifier.padding(paddingValues), viewModel = viewModel)
    }
}

// TODO: This does not seem right, but I could not find any info about non 3rd party solution
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ContentView(modifier: Modifier = Modifier, viewModel: NoteListViewModel) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.onIntent(NoteListIntent.LoadNoteList)
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = state is NoteListState.Loading,
        contentAlignment = Alignment.BottomEnd,
        onRefresh = {
            // TODO: currently it masks views while loading - do this only for first fetch
            viewModel.onIntent(NoteListIntent.LoadNoteList)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (val currentState = state) {
                is NoteListState.Loading -> {
                    LoadingNotesListView(
                        notes = currentState.mockedNotes
                    )
                }

                is NoteListState.Loaded -> {

                    if (currentState.notes.isEmpty()) {
                        Text(
                            "No notes available.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        NotesListView(notes = currentState.notes)
                    }
                }

                is NoteListState.Error -> {
                    Text(
                        currentState.errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {

                }
            }
        }
    }
}

@Composable
fun LoadingNotesListView(modifier: Modifier = Modifier, notes: List<Note>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(notes) { note ->
            NoteItem(true, note)
        }
    }
}

@Composable
fun NotesListView(notes: List<Note>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(notes) { note ->
            NoteItem(isLoading = false, note)
        }
    }
}

@Composable
fun NoteItem(isLoading: Boolean, note: Note) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(elevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.placeholder(
                    placeholderState = rememberPlaceholderState { !isLoading }
                ),
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                modifier = Modifier.placeholder(
                    placeholderState = rememberPlaceholderState { !isLoading }
                ),
                text = note.text,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
