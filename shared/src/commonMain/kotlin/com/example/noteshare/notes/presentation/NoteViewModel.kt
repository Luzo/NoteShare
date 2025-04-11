package com.example.noteshare.notes.presentation
import com.example.noteshare.firebase.FirebaseWrapper
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.model.NoteState
import com.example.noteshare.notes.usecase.LoadNotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val loadNotesUseCase: LoadNotesUseCase = LoadNotesUseCase()
) : CoroutineScope by MainScope() {

    private val _state = MutableStateFlow<NoteState>(NoteState.Loading(
        listOf(Note.mock(), Note.mock(), Note.mock())
    ))
    val state: StateFlow<NoteState> = _state.asStateFlow()

    fun onIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNotes -> {
                val mockedNotes = List(3) { Note.mock() }
                _state.value = NoteState.Loading(mockedNotes)

                launch { loadNotes() }
            }
        }
    }

    fun collectState(collector: (NoteState) -> Unit) {
        launch(Dispatchers.Main) {
            state.collect(collector)
        }
    }

    private suspend fun NoteViewModel.loadNotes() {
        try {
            // Load notes from use case
            val notes = loadNotesUseCase.execute()
            // Update state to Loaded with real notes
            _state.value = NoteState.Loaded(notes)
        } catch (e: Exception) {
            // Handle error by updating state
            _state.value = NoteState.Error("Failed to load notes: ${e.message}")
        }
    }
}
