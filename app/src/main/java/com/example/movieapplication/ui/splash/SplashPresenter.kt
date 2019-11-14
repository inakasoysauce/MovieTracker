package com.example.movieapplication.ui.splash

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashPresenter(view: SplashView?) : BasePresenter<SplashView>(view) {


    fun tryAutoLogin() {
        if (FirebaseInteractor.loggedIn()) {
            GlobalScope.launch(Dispatchers.IO) {
                val response = FirebaseInteractor.getUserInfo()
                if (response.isSuccessful) {
                    val user = response.body()
                    User.username = user?.username
                    User.picture_path = user?.picture_path
                    User.favouriteList = user?.favourites
                    User.ratingList = user?.ratings
                    User.friendList = user?.friends
                    User.sentRequests = user?.sent_requests
                    User.incomingRequests = user?.incoming_requests
                    view?.goToMainScreen()
                } else {
                    view?.goToMainScreen()
                }
            }
        } else {
            view?.goToMainScreen()
        }
    }
}