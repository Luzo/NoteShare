package com.example.noteshare.firebase

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.app
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize

actual object FirebaseInitializer {
    actual fun initialize(context: Any?) {
        // NOTE: Only works when using cocoapods
        // Firebase.initialize()
        // TODO: Create an injected object, that way it will be probably possible to init it from here
    }

    actual val firestore: FirebaseFirestore
        get() = Firebase.firestore
}