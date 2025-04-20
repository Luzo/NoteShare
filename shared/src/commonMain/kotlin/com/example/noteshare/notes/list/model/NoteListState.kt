package com.example.noteshare.notes.list.model

import com.example.noteshare.notes.model.Note

sealed class NoteListState {
    data class Loading(val mockedNotes: List<Note>) : NoteListState()
    data class Loaded(val notes: List<Note>) : NoteListState()
    data class Error(val errorMessage: String) : NoteListState()
}