package com.example.movieapplication.network.model

class PersonCredit {

    val cast: ArrayList<Credit>? = null

    class Credit {
        val character: String? = null
        val title: String? = null
            get() {
                return field ?: name
            }
        val name: String? = null
        val poster_path: String? = null
        val media_type: String? = null
        val id: Int? = null
    }
}