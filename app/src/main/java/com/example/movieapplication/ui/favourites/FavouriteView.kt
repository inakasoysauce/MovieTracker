package com.example.movieapplication.ui.favourites

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.SearchResultItem

interface FavouriteView : BaseView {

    fun showFavourites(list: ArrayList<SearchResultItem>)
    fun itemRemoved(id: String, type: String)
}