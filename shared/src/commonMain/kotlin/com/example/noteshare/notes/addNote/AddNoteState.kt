package com.example.noteshare.notes.addNote

sealed class AddNoteState {
    object Idle : AddNoteState()
    object Loading : AddNoteState()
    object Success : AddNoteState()
    data class Error(val message: String) : AddNoteState()
}