package com.example.noteshare.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noteshare.android.notes.list.NotesScreen
import com.example.noteshare.android.notes.addNote.AddNoteScreen
import com.example.noteshare.firebase.FirebaseInitializer
import com.example.noteshare.notes.addNote.AddNoteViewModel
import com.example.noteshare.notes.list.presentation.NoteListViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseInitializer.initialize(this)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

enum class NoteRoute {
    Notes,
    AddNote
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NoteRoute.Notes.name) {
        composable(NoteRoute.Notes.name) {
            NotesScreen(
                remember { NoteListViewModel() },
                router = navController
            )
        }
        composable(NoteRoute.AddNote.name) {
            AddNoteScreen(
                remember { AddNoteViewModel() },
                router = navController
            )
        }
    }
}