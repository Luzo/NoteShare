package com.example.noteshare.notes.list.presentation
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.navigation.NoteRoute
import com.example.noteshare.notes.navigation.NoteRouterViewModel
import com.example.noteshare.notes.shared.usecase.LoadNotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val loadNotesUseCase: LoadNotesUseCase = LoadNotesUseCase(),
    private val router: NoteRouterViewModel,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow<NoteListState>(
        NoteListState.Loading(
        listOf(Note.mock(), Note.mock(), Note.mock())
    ))
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    fun sendIntent(intent: NoteListIntent) {
        when (intent) {
            is NoteListIntent.LoadNoteList -> {
                val mockedNotes = List(3) { Note.mock() }
                _state.value = NoteListState.Loading(mockedNotes)

                scope.launch {
                    loadNotes()
                }
            }
            is NoteListIntent.AddNoteTapped -> {
                router.navigateTo(NoteRoute.AddNote)
            }
        }
    }

    // NOTE: Only used for iOS
    fun collectState(collector: (NoteListState) -> Unit) {
        scope.launch(Dispatchers.Main) {
            state.collect(collector)
        }
    }

    fun cancelScope() {
        scope.cancel()
    }

    private suspend fun NoteListViewModel.loadNotes() {
        try {
            // Load notes from use case
            val notes = loadNotesUseCase.execute()
            // Update state to Loaded with real notes
            _state.value = NoteListState.Loaded(notes)
        } catch (e: Exception) {
            // Handle error by updating state
            _state.value = NoteListState.Error("Failed to load notes: ${e.message}")
        }
    }
}
