package com.example.movieapplication.ui.choose_friend

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.FriendItem

interface ChooseFriendView : BaseView {

    fun showFriends(list: ArrayList<FriendItem>?)
}