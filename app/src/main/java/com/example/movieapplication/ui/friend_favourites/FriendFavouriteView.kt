package com.example.movieapplication.ui.friend_favourites

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.SearchResultItem

interface FriendFavouriteView : BaseView {

    fun showFavourites(list: ArrayList<SearchResultItem>)
}