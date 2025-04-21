@file:OptIn(ExperimentalWearMaterialApi::class, ExperimentalMaterial3Api::class)

package com.example.noteshare.android.notes.list

import LoadingNotesListView
import NotesListView
import android.widget.ScrollView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.ui.Alignment
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.list.presentation.NoteListIntent
import com.example.noteshare.notes.list.presentation.NoteListViewModel
import androidx.compose.runtime.DisposableEffect

@Composable
fun NotesScreen(viewModel: NoteListViewModel) {
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
                        viewModel.sendIntent(NoteListIntent.AddNoteTapped)
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
        viewModel.sendIntent(NoteListIntent.LoadNoteList)
        viewModel.sendIntent(NoteListIntent.ObserveNoteChanges)
    }

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = state is NoteListState.Loading,
        contentAlignment = Alignment.BottomEnd,
        onRefresh = {
            // TODO: currently it masks views while loading - do this only for first fetch
            viewModel.sendIntent(NoteListIntent.LoadNoteList)
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                "No notes available.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    } else {
                        NotesListView(notes = currentState.notes, viewModel = viewModel)
                    }
                }

                is NoteListState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            currentState.errorMessage,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {

                }
            }
        }
    }
}
