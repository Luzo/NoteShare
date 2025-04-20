package com.example.noteshare.notes.list.presentation

sealed class NoteListIntent {
    object LoadNoteList : NoteListIntent()
    object AddNoteTapped : NoteListIntent()
}