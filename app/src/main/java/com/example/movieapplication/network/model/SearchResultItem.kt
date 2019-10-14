package com.example.movieapplication.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class SearchResultItem : Parcelable {
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchResultItem> {
        override fun createFromParcel(parcel: Parcel): SearchResultItem {
            return SearchResultItem()
        }

        override fun newArray(size: Int): Array<SearchResultItem?> {
            return arrayOfNulls(size)
        }
    }
}