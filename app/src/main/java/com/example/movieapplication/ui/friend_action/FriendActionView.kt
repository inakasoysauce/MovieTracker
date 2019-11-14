package com.example.movieapplication.ui.friend_action

import com.example.movieapplication.base.BaseView

interface FriendActionView : BaseView {
    fun showAsFriend(username: String, picturePath: String?)
    fun showAsRequested(username: String, picturePath: String?)
    fun showAsRequestSent(username: String, picturePath: String?)
    fun showAsDefault(username: String, picturePath: String?)
}