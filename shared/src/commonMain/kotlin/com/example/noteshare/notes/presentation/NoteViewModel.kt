package com.example.noteshare.notes.presentation
import com.example.noteshare.notes.model.NoteState
import com.example.noteshare.notes.usecase.LoadNotesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NoteViewModel(
    private val loadNotesUseCase: LoadNotesUseCase = LoadNotesUseCase()
) : CoroutineScope by MainScope() {

    private val _state = MutableStateFlow(NoteState.Empty)
    val state: StateFlow<NoteState> = _state.asStateFlow()

    fun onIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.LoadNotes -> {
                _state.value = NoteReducer.reduce(_state.value, intent)
                launch {
                    val notes = loadNotesUseCase.execute()
                    _state.value = NoteReducer.reduce(_state.value, intent, notes)
                }
            }
        }
    }

    fun collectState(collector: (NoteState) -> Unit) {
        launch(Dispatchers.Main) {
            state.collect(collector)
        }
    }
}