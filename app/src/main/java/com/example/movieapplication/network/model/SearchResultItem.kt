package com.example.movieapplication.network.model

import android.content.Context
import com.example.movieapplication.R
import com.google.gson.annotations.SerializedName

open class SearchResultItem {

    val id: Int? = null

    companion object {
        const val tvShow = "tv"
        const val movie = "movie"
        const val person = "person"
    }

    @SerializedName("media_type")
    val mediaType: String? = null
        get() {
            return field ?: getType()
        }

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
            return field ?: name

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

    val birthday: String? = null

    private fun getType(): String {
        if (name != null) {
            if (birthday != null) {
                return person
            }
            return tvShow
        }
        return movie
    }

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
        if (birthday != null)
            return birthday
        return ""
    }

    fun isMovie(): Boolean = mediaType == movie
    fun isTvShow(): Boolean = mediaType == tvShow
    fun isPerson(): Boolean = mediaType == person
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

