package com.example.noteshare.notes.usecase
import com.example.noteshare.firebase.FirebaseWrapper
import com.example.noteshare.notes.model.Note
import kotlinx.coroutines.delay
import kotlin.random.Random

class LoadNotesUseCase {
    suspend fun execute(): List<Note> {
        return FirebaseWrapper.loadDocuments<Note>("notes")
    }
}
