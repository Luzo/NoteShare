package com.example.noteshare

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform