package com.example.movieapplication.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SearchResultItem {
    val id: Int? = null

    @SerializedName("poster_path")
    val posterPath: String? = null

    @SerializedName("release_date")
    val releaseDate: String? = null

    val title: String? = null

    @SerializedName("vote_average")
    val voteAverage: Double? = null

    @SerializedName("vote_count")
    val voteCount: Int? = null
}