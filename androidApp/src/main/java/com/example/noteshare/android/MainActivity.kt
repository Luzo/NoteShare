package com.example.noteshare.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.noteshare.android.notes.MainScreen
import com.example.noteshare.firebase.FirebaseInitializer
import com.example.noteshare.firebase.FirebaseWrapper
import com.example.noteshare.notes.presentation.NoteViewModel

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
                    MainScreen(NoteViewModel())
                }
            }
        }
    }
}
