package com.example.movieapplication.network.model

class MovieOrSeries {
    val genres: ArrayList<Genre>? = null
    val overview: String? = null
    val poster_path: String? = null
    val release_date: String? = null
        get() {
            return field ?: first_air_date
        }
    val title: String? = null
        get() {
            return field ?: name
        }
    val name: String? = null
    val vote_average: Double? = null
    val first_air_date: String? = null
    val vote_count: Int? = null
    val id: Int? = null
    val type: String
        get() {
            return if (title == name)
                "tv"
            else
                "movie"
        }

    data class Genre(
        val id: Int,
        val name: String
    )
}