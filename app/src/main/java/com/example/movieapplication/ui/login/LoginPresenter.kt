package com.example.movieapplication.ui.login

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(view: LoginView?) : BasePresenter<LoginView>(view) {

    fun attemptLogin(email: String, password: String) {
        FirebaseInteractor.login(email, password, {
            getUserData()
        }, {
            view?.showError(it)
        })
    }

    fun attemptRegister(email: String, password: String, username: String) {
        FirebaseInteractor.createUser(email, password, username, {
            view?.success()
        }, {
            view?.showError(it)
        })
    }

    private fun getUserData() {
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
                withContext(Dispatchers.Main) {
                    view?.success()
                }
            } else {
                withContext(Dispatchers.Main) {
                    view?.showError("Couldn't load user info")
                }
            }
        }
    }
}