package com.example.noteshare.notes.model
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String = generateRandomId(),
    val title: String,
    val text: String
) {
    companion object {
        fun mock(): Note {
            return Note(
                id = generateRandomId(),
                title = "This is a mock",
                text = "This is a mock to show text"
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun generateRandomId(): String = Uuid.random().toString()
