package com.example.noteshare.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.*
import com.example.noteshare.notes.presentation.NoteIntent
import com.example.noteshare.notes.presentation.NoteViewModel

@Composable
fun MainScreen(viewModel: NoteViewModel) {
    // Collect the state from the ViewModel
    val state by viewModel.state.collectAsState()

    // Trigger the load notes intent when the screen is launched
    LaunchedEffect(Unit) {
        viewModel.onIntent(NoteIntent.LoadNotes)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Reload button to trigger loading of notes again
        Button(
            onClick = { viewModel.onIntent(NoteIntent.LoadNotes) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reload")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show loading state
        if (state.isLoading) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(vertical = 4.dp)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
            }
        } else {
            // Show list of notes
            LazyColumn {
                items(state.notes) { note ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(note.title, style = MaterialTheme.typography.titleMedium)
                        Text(note.text, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Show error message if exists
        state.errorMessage?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
