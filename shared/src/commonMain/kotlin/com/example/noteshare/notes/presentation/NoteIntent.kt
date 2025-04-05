package com.example.noteshare.notes.presentation

sealed class NoteIntent {
    object LoadNotes : NoteIntent()
}