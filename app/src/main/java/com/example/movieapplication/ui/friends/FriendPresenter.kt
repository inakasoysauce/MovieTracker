package com.example.movieapplication.ui.friends

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import com.example.movieapplication.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendPresenter(view: FriendView) : BasePresenter<FriendView>(view) {

    fun getUsers(type: String) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            when (type) {
                Constants.ALL -> {
                    val response = FirebaseInteractor.getAllUser()
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        if (response.isSuccessful) {
                            view?.showPeople(response.body())
                        } else {
                            view?.showError("Couldn't load info")
                        }
                    }
                }
                Constants.FRIENDS -> {
                    val response = FirebaseInteractor.getFriends()
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        if (response.isSuccessful) {
                            User.addFriendsAll(response.body())
                            view?.showPeople(response.body())
                        } else {
                            view?.showError("Couldn't load info")
                        }
                    }
                }
                else -> {
                    val response = FirebaseInteractor.getReceivedRequests()
                    withContext(Dispatchers.Main) {
                        view?.hideLoading()
                        if (response.isSuccessful) {
                            User.addReceivedRequestsAll(response.body())
                            view?.showPeople(response.body())
                        } else {
                            view?.showError("Couldn't load info")
                        }
                    }
                }
            }
        }
    }

    fun sendRequest(user: String) {
        view?.showLoading()
        FirebaseInteractor.sendRequest(user, {
            User.sentRequests?.add(user)
            view?.hideLoading()
            view?.update(user)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun acceptRequest(user: String) {
        view?.showLoading()
        FirebaseInteractor.acceptRequest(user, {
            User.requestAccepted(user)
            view?.hideLoading()
            view?.update(user)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun deleteRequest(user: String) {
        view?.showLoading()
        FirebaseInteractor.deleteRequest(user, FirebaseInteractor.getUID(), {
            User.requestDeleted(user)
            view?.hideLoading()
            view?.update(user)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun onNewRequest(user: String) {
        User.addToIncomingRequests(user)
        view?.update(user)
    }

    fun onFriendAcceptedRequest(user: String) {
        User.moveToFriends(user)
        view?.update(user)
    }
}