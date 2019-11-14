package com.example.movieapplication.ui.choose_friend

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseFriendPresenter(view: ChooseFriendView) : BasePresenter<ChooseFriendView>(view) {

    fun getFriends() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = FirebaseInteractor.getFriends()
            withContext(Dispatchers.Main) {
                view?.hideLoading()
                if (response.isSuccessful) {
                    view?.showFriends(response.body())
                } else {
                    view?.showError("Couldn't load info")
                }
            }
        }
    }
}