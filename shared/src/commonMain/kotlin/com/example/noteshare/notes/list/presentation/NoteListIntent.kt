package com.example.noteshare.notes.list.presentation

import com.example.noteshare.notes.model.Note

sealed class NoteListIntent {
    object LoadNoteList : NoteListIntent()
    object AddNoteTapped : NoteListIntent()
    data class DeleteNoteTapped(val note: Note) : NoteListIntent()
    object ObserveNoteChanges : NoteListIntent()
}