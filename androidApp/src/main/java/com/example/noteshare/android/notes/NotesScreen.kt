@file:OptIn(ExperimentalWearMaterialApi::class)

package com.example.noteshare.android.notes

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.placeholder
import androidx.wear.compose.material.rememberPlaceholderState
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.model.NoteState
import com.example.noteshare.notes.presentation.NoteIntent
import com.example.noteshare.notes.presentation.NoteViewModel

@Composable
fun MainScreen(viewModel: NoteViewModel) {
    ContentView(modifier = Modifier.padding(top = 56.dp), viewModel = viewModel)
}

// TODO: This does not seem right, but I could not find any info about non 3rd party solution
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ContentView(modifier: Modifier = Modifier, viewModel: NoteViewModel) {
    val state by viewModel.state.collectAsState()
    val loading = rememberPlaceholderState { !(state is NoteState.Loading) }
    LaunchedEffect(Unit) {
        viewModel.onIntent(NoteIntent.LoadNotes)
    }

    LaunchedEffect(loading) {
        loading.startPlaceholderAnimation()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (val currentState = state) {
            is NoteState.Loading -> {
                LoadingNotesListView(
                    notes = currentState.mockedNotes
                )
            }

            is NoteState.Loaded -> {
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

            is NoteState.Error -> {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier
            .background(
                Brush.verticalGradient(
                    0.0f to Color.White.copy(alpha = 0.6f),
                    1.0f to Color.White,
                    startY = 0.0f,
                    endY = 100.0f
                )
            )
        ) {
            Button(
                onClick = { viewModel.onIntent(NoteIntent.LoadNotes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 32.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text(text = "Reload", color = Color.White)
            }
        }
    }
}

@Composable
fun LoadingNotesListView(notes: List<Note>) {
    LazyColumn(
        modifier = Modifier
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
