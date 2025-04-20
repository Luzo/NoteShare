package com.example.noteshare.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.noteshare.android.notes.list.NotesScreen
import com.example.noteshare.android.notes.addNote.AddNoteScreen
import com.example.noteshare.firebase.FirebaseInitializer
import com.example.noteshare.notes.addNote.AddNoteViewModel
import com.example.noteshare.notes.list.presentation.NoteListViewModel
import com.example.noteshare.notes.navigation.*

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val router = remember { NoteRouterViewModel() }
    val currentScreen by router.currentScreen.collectAsState()

    val direction by router.navigationDirection.collectAsState()

    AnimatedContent (
        targetState = currentScreen,
        transitionSpec = {
            val directionVector = direction.value
            val inAnimation = slideInHorizontally(
                animationSpec = tween(500)
            ) { it * directionVector } + fadeIn()
            val outAnimation = slideOutHorizontally(
                animationSpec = tween(500)
            ) { -it * directionVector } + fadeOut()
            inAnimation with outAnimation
        },
        content = { screen ->
            when (screen) {
                NoteRoute.NotesList -> {
                    val viewModel = remember { NoteListViewModel(router = router) }
                    NotesScreen(
                        viewModel = viewModel,
                    )
                }

                NoteRoute.AddNote -> {
                    val viewModel = remember { AddNoteViewModel(router = router) }
                    AddNoteScreen(
                        viewModel = viewModel,
                    )
                }
            }
        }
    )
}