package com.example.movieapplication.ui.search

import android.content.Context
import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.SearchResultItem
import java.util.ArrayList

interface ISearchScreen : BaseView {
    fun noResult()
    fun addMovies(movies: ArrayList<SearchResultItem>?)
    fun getAppContext() : Context
    fun showNoConnectionMessage()
}