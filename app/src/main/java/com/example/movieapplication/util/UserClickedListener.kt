package com.example.movieapplication.util

import com.example.movieapplication.base.BaseAdapterListener
import com.example.movieapplication.network.model.FriendItem

interface UserClickedListener : BaseAdapterListener {
    fun userClicked(user: FriendItem)
    fun sendRequest(user: String)
    fun acceptRequest(user: String)
    fun deleteRequest(user: String)
    fun reload()
}