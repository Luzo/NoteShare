package com.example.noteshare.firebase

import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.serialization.Serializable

expect object FirebaseInitializer {

    fun initialize(context: Any?)

    val firestore: FirebaseFirestore
}

object FirebaseWrapper {
    suspend inline fun <reified T : @Serializable Any>loadDocuments(collectionPath: String): List<T> {
        val firestore = FirebaseInitializer.firestore
        return firestore
            .collection(collectionPath)
            .get()
            .documents
            .map { it.data<T>() }
    }
}
