package com.example.noteshare.firebase

import dev.gitlive.firebase.firestore.FirebaseFirestore

expect object FirebaseInitializer {

    fun initialize(context: Any?)

    val firestore: FirebaseFirestore
}