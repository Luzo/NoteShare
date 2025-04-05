package com.example.noteshare.notes.usecase
import com.example.noteshare.notes.model.Note
import kotlinx.coroutines.delay
import kotlin.random.Random

class LoadNotesUseCase {
    suspend fun execute(): List<Note> {
        val titles = listOf("Buy Milk", "Read Book", "Build KMP App", "Call Dad", "Fix Bug", "Refactor Code")
        // simulate loading
        delay(1500)

        val numberOfItems = Random.nextInt(from =  0, until = 10)
        return List(numberOfItems) { index ->
            val title = titles.random()
            Note(
                id = index.toString(),
                title = title,
                text = "Sample text for $title"
            )
        }
    }
}
