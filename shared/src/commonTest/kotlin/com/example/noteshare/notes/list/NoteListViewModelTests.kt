package com.example.noteshare.notes.list.presentation

import app.cash.turbine.test
import com.example.noteshare.notes.list.model.NoteListState
import com.example.noteshare.notes.model.Note
import com.example.noteshare.notes.navigation.NavigationDirection
import com.example.noteshare.notes.navigation.NoteRoute
import com.example.noteshare.notes.navigation.NoteRouterViewModel
import com.example.noteshare.notes.shared.usecase.DeleteNoteUseCase
import com.example.noteshare.notes.shared.usecase.LoadNotesUseCase
import com.example.noteshare.notes.shared.usecase.ObserveNoteUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NoteListViewModelTest {

    private val fakeRouter = object : NoteRouterViewModel() {
        val navigatedTo = mutableListOf<NoteRoute>()
        override fun navigateTo(screen: NoteRoute, direction: NavigationDirection) {
            navigatedTo.add(screen)
            super.navigateTo(screen, direction)
        }
    }

    private val mockNotes = listOf(
        Note.mock(id = "a", title = "a", text = "a"),
        Note.mock(id = "b", title = "b", text = "b")
    )
    private val fakeLoadNotesUseCase = object : LoadNotesUseCase() {
        override suspend fun execute(): List<Note> = mockNotes
    }

    private var deletedNote: Note? = null
    private val fakeDeleteNoteUseCase = object : DeleteNoteUseCase() {
        override suspend fun execute(note: Note) {
            deletedNote = note
        }
    }

    private val noteFlow = MutableSharedFlow<List<Note>>(replay = 1)
    private val fakeObserveNoteUseCase = object : ObserveNoteUseCase() {
        override fun execute(): Flow<List<Note>> = noteFlow
    }

    @Test
    fun loadNotes_setsLoadedState() = runTest {
        val viewModel = NoteListViewModel(
            router = fakeRouter,
            loadNotesUseCase = fakeLoadNotesUseCase,
            deleteNotesUseCase = fakeDeleteNoteUseCase,
            observeNotesUseCase = fakeObserveNoteUseCase
        )


        viewModel.state.test {
            viewModel.sendIntent(NoteListIntent.LoadNoteList)
            // Skips mocked values
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading is NoteListState.Loading)

            val loaded = awaitItem()
            assertTrue(loaded is NoteListState.Loaded)

            val currentState = viewModel.state.value
            assertEquals(NoteListState.Loaded(mockNotes), currentState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun addNoteTapped_navigatesToAddNote() = runTest {
        val viewModel = NoteListViewModel(
            router = fakeRouter,
            loadNotesUseCase = fakeLoadNotesUseCase,
            deleteNotesUseCase = fakeDeleteNoteUseCase,
            observeNotesUseCase = fakeObserveNoteUseCase
        )

        viewModel.sendIntent(NoteListIntent.AddNoteTapped)

        assertEquals(listOf(NoteRoute.AddNote), fakeRouter.navigatedTo)
    }

    @Test
    fun deleteNoteTapped_callsDeleteUseCase() = runTest {
        val viewModel = NoteListViewModel(
            router = fakeRouter,
            loadNotesUseCase = fakeLoadNotesUseCase,
            deleteNotesUseCase = fakeDeleteNoteUseCase,
            observeNotesUseCase = fakeObserveNoteUseCase
        )

        val note = Note.mock()
        viewModel.sendIntent(NoteListIntent.DeleteNoteTapped(note))

        // Allow coroutine to finish
        kotlinx.coroutines.delay(10)

        assertEquals(note, deletedNote)
    }

    @Test
    fun observeNoteChanges_updatesState() = runTest {
        val viewModel = NoteListViewModel(
            router = fakeRouter,
            loadNotesUseCase = fakeLoadNotesUseCase,
            deleteNotesUseCase = fakeDeleteNoteUseCase,
            observeNotesUseCase = fakeObserveNoteUseCase
        )

        val updatedNotes = listOf(Note.mock(), Note.mock(), Note.mock())

        viewModel.sendIntent(NoteListIntent.ObserveNoteChanges)
        noteFlow.emit(updatedNotes)

        kotlinx.coroutines.delay(10)

        assertEquals(NoteListState.Loaded(updatedNotes), viewModel.state.value)
    }
}
