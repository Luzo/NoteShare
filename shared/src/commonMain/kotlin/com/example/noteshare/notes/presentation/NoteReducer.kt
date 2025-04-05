package com.example.noteshare.notes.presentation

import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.model.NoteState

object NoteReducer {
    fun reduce(state: NoteState, intent: NoteIntent, result: List<Note>? = null): NoteState {
        return when (intent) {
            is NoteIntent.LoadNotes -> {
                if (result == null) {
                    state.copy(isLoading = true)
                } else {
                    state.copy(notes = result, isLoading = false)
                }
            }
        }
    }
}