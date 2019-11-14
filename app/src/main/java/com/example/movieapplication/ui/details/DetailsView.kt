package com.example.movieapplication.ui.details

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.MovieOrSeries
import com.example.movieapplication.network.model.CastResponse

interface DetailsView : BaseView {
    fun showDetails(movie: MovieOrSeries?)
    fun showSimilarMoviesOrSeries(moviesOrSeries: ArrayList<MovieOrSeries>?)
    fun addedToFavourites()
    fun rateDeleted()
}