package com.example.noteshare.notes.shared.usecase
import com.example.noteshare.firebase.FirestoreWrapper
import com.example.noteshare.notes.model.Note

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
