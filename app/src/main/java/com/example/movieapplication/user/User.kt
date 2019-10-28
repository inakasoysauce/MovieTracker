package com.example.movieapplication.user

import android.graphics.Bitmap

object User {
    var username: String? = null
        set(value) {
            field = value
            loggedIn = field != null
        }
    var loggedIn: Boolean = false
    var profilePicture : Bitmap? = null
}