package com.example.noteshare.notes.model

data class NoteState(
    val notes: List<Note> = emptyList(),
    var isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        val Empty = NoteState()
        val Loading = NoteState(isLoading = true)
    }
}
