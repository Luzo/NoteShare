package com.example.noteshare.android.notes.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.placeholder
import androidx.wear.compose.material.rememberPlaceholderState
import com.example.noteshare.notes.model.Note
import kotlinx.coroutines.launch

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun NoteItem(isLoading: Boolean, note: Note, onDeleteTapped: (Note) -> Unit) {
    val swipeState = rememberSwipeToDismissBoxState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            scope.launch {
                swipeState.reset()
                onDeleteTapped(note)
            }
        }
    }

    SwipeToDismissBox (
        state = swipeState,
        enableDismissFromStartToEnd = false,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        backgroundContent = {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Red)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 40.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        content = {
            Card(
                modifier = Modifier
                    .shadow(elevation = 4.dp)
                    .fillMaxWidth(),
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
    )
}
