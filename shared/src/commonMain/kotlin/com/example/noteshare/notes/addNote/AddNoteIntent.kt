package com.example.noteshare.notes.addNote
import com.example.noteshare.notes.model.Note

sealed class AddNoteIntent {
    data class AddNote(val note: Note) : AddNoteIntent()
    object Reset : AddNoteIntent()
    object GoBackTapped : AddNoteIntent()
}