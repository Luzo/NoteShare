package com.example.noteshare.notes.addNote
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.navigation.NoteRoute
import com.example.noteshare.notes.navigation.NoteRouterViewModel
import com.example.noteshare.notes.shared.usecase.AddNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val addNoteUseCase: AddNoteUseCase = AddNoteUseCase(),
    private val router: NoteRouterViewModel,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow<AddNoteState>(AddNoteState.Idle)
    val state: StateFlow<AddNoteState> = _state.asStateFlow()

    fun sendIntent(intent: AddNoteIntent) {
        when (intent) {
            is AddNoteIntent.AddNote -> {
                scope.launch {
                    _state.value = AddNoteState.Loading
                    try {
                        addNoteUseCase.execute(intent.note)
                        _state.value = AddNoteState.Success
                        router.popBack()
                    } catch (e: Exception) {
                        _state.value = AddNoteState.Error(e.message ?: "Something went wrong")
                    }
                }
            }
            is AddNoteIntent.Reset -> {
                _state.value = AddNoteState.Idle
            }
            is AddNoteIntent.GoBackTapped -> {
                router.popBack()
            }
        }
    }
}