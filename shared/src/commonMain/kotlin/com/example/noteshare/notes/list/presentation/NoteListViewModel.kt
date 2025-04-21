package com.example.noteshare.notes.list.presentation
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.navigation.NoteRoute
import com.example.noteshare.notes.navigation.NoteRouterViewModel
import com.example.noteshare.notes.shared.usecase.DeleteNoteUseCase
import com.example.noteshare.notes.shared.usecase.LoadNotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val loadNotesUseCase: LoadNotesUseCase = LoadNotesUseCase(),
    private val deleteNotesUseCase: DeleteNoteUseCase = DeleteNoteUseCase(),
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
            is NoteListIntent.DeleteNoteTapped -> {
                scope.launch {
                    deleteNote(intent.note)
                }
            }
        }
    }

    // NOTE: Only used for iOS
    fun collectState(collector: (NoteListState) -> Unit) {
        scope.launch(Dispatchers.Main) {
            state.collectLatest { collector(it) }
        }
    }

    fun cancelScope() {
        scope.cancel()
    }

    private suspend fun NoteListViewModel.loadNotes() {
        try {
            val notes = loadNotesUseCase.execute()
            _state.value = NoteListState.Loaded(notes)
        } catch (e: Exception) {
            _state.value = NoteListState.Error("Failed to load notes: ${e.message}")
        }
    }

    private suspend fun NoteListViewModel.deleteNote(note: Note) {
        try {
            deleteNotesUseCase.execute(note)
            loadNotes()
        } catch (e: Exception) {
            _state.value = NoteListState.Error("Failed to load notes: ${e.message}")
        }
    }
}
