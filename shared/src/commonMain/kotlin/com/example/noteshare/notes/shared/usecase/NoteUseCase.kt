package com.example.noteshare.notes.shared.usecase
import com.example.noteshare.firebase.FirestoreWrapper
import com.example.noteshare.notes.model.Note
import kotlinx.coroutines.flow.Flow

class LoadNotesUseCase {
    suspend fun execute(): List<Note> {
        return FirestoreWrapper.loadDocuments<Note>("notes")
    }
}

class AddNoteUseCase {
    suspend fun execute(note: Note) {
        return FirestoreWrapper.add<Note>(note, "notes")
    }
}

class DeleteNoteUseCase {
    suspend fun execute(note: Note) {
        return FirestoreWrapper.delete<Note>(note, "notes")
    }
}

class ObserveNoteUseCase {
    fun execute(): Flow<List<Note>> {
        return FirestoreWrapper.observeChanges<Note>("notes")
    }
}