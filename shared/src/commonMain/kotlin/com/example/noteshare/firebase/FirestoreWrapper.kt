package com.example.noteshare.firebase

import com.example.noteshare.notes.model.Identifiable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import co.touchlab.skie.configuration.annotations.SuspendInterop

object FirestoreWrapper {
    @SuspendInterop.Disabled
    suspend inline fun <reified T : Identifiable>loadDocuments(collectionPath: String): List<T> {
        return FirebaseInitializer.firestore
            .collection(collectionPath)
            .get()
            .documents
            .map {
                it.data<T>()
            }
    }

    @SuspendInterop.Disabled
    suspend inline fun <reified T : Identifiable>add(item: T, collectionPath: String,) {
        FirebaseInitializer.firestore
            .collection(collectionPath)
            .document(item.id)
            .set(item)
    }

    @SuspendInterop.Disabled
    suspend inline fun <reified T : Identifiable>delete(item: T, collectionPath: String,) {
        FirebaseInitializer.firestore
            .collection(collectionPath)
            .document(item.id)
            .delete()
    }

    inline fun <reified T : Identifiable>observeChanges(collectionPath: String): Flow<List<T>> {
        return FirebaseInitializer.firestore
            .collection(collectionPath)
            .snapshots
            .map {
                it.documents
                    .map {
                        it.data<T>()
                    }
            }
    }
}
