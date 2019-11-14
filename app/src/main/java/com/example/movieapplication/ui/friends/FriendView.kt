package com.example.movieapplication.ui.friends

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.FriendItem

interface FriendView : BaseView {
    fun showPeople(people: ArrayList<FriendItem>?)
    fun update(user: String)
}