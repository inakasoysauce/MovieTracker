package com.example.movieapplication.util

import com.example.movieapplication.base.BaseAdapterListener

interface FriendChooseListener : BaseAdapterListener {

    fun friendChoosed(friendID: String)
}