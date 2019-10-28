package com.example.movieapplication.network.model

data class Movie(
    val genres: ArrayList<Genre>,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val budget: Int
) {
    data class Genre(
        val id: Int,
        val name: String
    )
}