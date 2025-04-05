package com.example.noteshare.notes.model

sealed class NoteState {
    data class Loading(val mockedNotes: List<Note>) : NoteState()
    data class Loaded(val notes: List<Note>) : NoteState()
    data class Error(val errorMessage: String) : NoteState()
}