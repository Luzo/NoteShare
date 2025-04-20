package com.example.noteshare.firebase

import kotlinx.serialization.Serializable

object FirestoreWrapper {
    suspend inline fun <reified T : @Serializable Any>loadDocuments(collectionPath: String): List<T> {
        return FirebaseInitializer.firestore
            .collection(collectionPath)
            .get()
            .documents
            .map { it.data<T>() }
    }

    suspend inline fun <reified T : @Serializable Any>add(item: T, collectionPath: String,) {
        FirebaseInitializer.firestore
            .collection(collectionPath)
            .add(item)
    }
}
