package com.example.noteshare.notes.model
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
sealed interface Identifiable {
    val id: String
}

@Serializable
data class Note (
    override val id: String,
    val title: String,
    val text: String
): Identifiable {
    constructor(
        title: String,
        text: String
    ): this(
        id = generateRandomId(),
        title = title,
        text = text
    )

    companion object {
        fun mock(): Note {
            return Note(
                id = generateRandomId(),
                title = "This is a mock",
                text = "This is a mock to show text"
            )
        }

        fun mock(id: String = "", title: String = "", text: String = ""): Note {
            return Note(
                id = id,
                title = title,
                text = text
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun generateRandomId(): String = Uuid.random().toString()
