package com.example.movieapplication.user

object User {
    var username: String? = null
        set(value) {
            field = value
            loggedIn = field != null
        }
    var loggedIn: Boolean = false
}