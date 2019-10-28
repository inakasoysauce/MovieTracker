package com.example.movieapplication.network.model

import android.content.Context
import com.example.movieapplication.R
import com.google.gson.annotations.SerializedName

class SearchResultItem {

    val id: Int? = null

    @SerializedName("media_type")
    val mediaType: String? = null

    @SerializedName("poster_path")
    val posterPath: String? = null
        get() {
            return if (mediaType == person)
                profilePath
            else
                field
        }

    @SerializedName("profile_path")
    val profilePath: String? = null

    @SerializedName("release_date")
    val releaseDate: String? = null

    val title: String? = null
        get() {
            return if (mediaType == movie) {
                field
            } else
                name

        }

    val name: String? = null

    @SerializedName("vote_average")
    val voteAverage: Double? = null

    @SerializedName("vote_count")
    val voteCount: Int? = null

    @SerializedName("first_air_date")
    val firstAirDate: String? = null

    @SerializedName("popularity")
    val popularity: Double? = null

    @SerializedName("known_for")
    val knownForList: ArrayList<KnownFor>? = null

    private val tvShow = "tv"
    private val movie = "movie"
    private val person = "person"

    fun getFirstInfo(): String? {
        return when (mediaType) {
            movie -> releaseDate
            tvShow -> firstAirDate
            person -> {
                getFirstKnownFor()
            }
            else -> ""
        }
    }

    fun getSecondInfo(context: Context): String? {
        return if (mediaType != person) {
            context.getString(R.string.search_result_rating, voteAverage?.toString(), voteCount?.toString())
        } else {
            popularity.toString()
        }
    }

    private fun getFirstKnownFor(): String? {
        knownForList?.let {
            if (it.isNotEmpty())
                return it[0].title
        }
        return ""
    }
}

class KnownFor {

    val title: String? = null
        get() {
            return if (mediaType == "movie") {
                field
            } else
                name
        }

    val name: String? = null

    @SerializedName("media_type")
    val mediaType: String? = null
}

