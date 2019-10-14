package com.example.movieapplication.ui.details

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast

interface IDetailsScreen : BaseView {
    fun showDetails(movie: Movie?)
    fun showCast(cast_list : ArrayList<MovieCast.Cast>?)
}