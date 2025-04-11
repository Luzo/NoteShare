package com.example.noteshare.firebase

import android.content.Context
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize

actual object FirebaseInitializer {

    actual fun initialize(context: Any?) {
        Firebase.initialize(context = context as? Context)
    }

    actual val firestore: FirebaseFirestore
        get() = Firebase.firestore
}