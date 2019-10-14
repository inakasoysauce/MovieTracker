package com.example.movieapplication.network.model

data class MovieCast(
    val cast: ArrayList<Cast>,
    val id: Int
) {
    data class Cast(
        val character: String,
        val name: String,
        val profile_path: String
    )
}