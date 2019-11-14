package com.example.movieapplication.ui.search

import android.content.Context
import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.SearchResultItem
import java.util.ArrayList

interface SearchView : BaseView {
    fun noResult()
    fun addSearchResults(movies: ArrayList<SearchResultItem>?)
    fun showNoConnectionMessage()
}