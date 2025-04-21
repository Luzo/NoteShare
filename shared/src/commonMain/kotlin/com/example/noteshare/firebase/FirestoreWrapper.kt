package com.example.noteshare.firebase

import com.example.noteshare.notes.model.Identifiable

object FirestoreWrapper {
    suspend inline fun <reified T : Identifiable>loadDocuments(collectionPath: String): List<T> {
        return FirebaseInitializer.firestore
            .collection(collectionPath)
            .get()
            .documents
            .map {
                it.reference.id
                it.data<T>()
            }
    }

    suspend inline fun <reified T : Identifiable>add(item: T, collectionPath: String,) {
        FirebaseInitializer.firestore
            .collection("notes")
            .document(item.id)
            .set(item)
    }

    suspend inline fun <reified T : Identifiable>delete(item: T, collectionPath: String,) {
        FirebaseInitializer.firestore
            .collection(collectionPath)
            .document(item.id)
            .delete()
    }
}
