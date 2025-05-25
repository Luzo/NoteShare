package com.example.noteshare.notes.list.presentation
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.navigation.NoteRoute
import com.example.noteshare.notes.navigation.NoteRouterViewModel
import com.example.noteshare.notes.shared.usecase.DeleteNoteUseCase
import com.example.noteshare.notes.shared.usecase.LoadNotesUseCase
import com.example.noteshare.notes.shared.usecase.ObserveNoteUseCase
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
    private val router: NoteRouterViewModel,
    private val loadNotesUseCase: LoadNotesUseCase = LoadNotesUseCase(),
    private val deleteNotesUseCase: DeleteNoteUseCase = DeleteNoteUseCase(),
    private val observeNotesUseCase: ObserveNoteUseCase = ObserveNoteUseCase()
) {
    // TODO: Loading is probably unnecessary since we observe the changes - we just nee to switch state to Loading....
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow<NoteListState>(
        NoteListState.Loading(
            listOf(Note.mock(), Note.mock(), Note.mock())
        )
    )
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

            is NoteListIntent.ObserveNoteChanges -> {
                scope.launch {
                    observeNotes()
                }
            }
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
        } catch (e: Exception) {
            _state.value = NoteListState.Error("Failed to load notes: ${e.message}")
        }
    }

    private suspend fun NoteListViewModel.observeNotes() {
        observeNotesUseCase.execute()
            .collectLatest {
                _state.value = NoteListState.Loaded(it)
            }
    }
}