package com.example.movieapplication.ui.friend_action

import android.os.Bundle
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.event_bus.Events
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import com.example.movieapplication.util.NotificationEvent

class FriendActionPresenter(view: FriendActionView) : BasePresenter<FriendActionView>(view) {

    var uid: String? = null
    var username: String? = null
    private var picturePath: String? = null

    fun checkStatus(arguments: Bundle?) {
        arguments?.let {
            uid = it.getString("uid")
            username = it.getString("username")
            picturePath = it.getString("picture_path")
            check(uid!!)
        }
    }

    private fun check(uid: String) {
        when {
            User.alreadyFriend(uid) -> view?.showAsFriend(username!!, picturePath)
            User.alreadyRequestReceived(uid) -> view?.showAsRequested(username!!, picturePath)
            User.alreadySentRequest(uid) -> view?.showAsRequestSent(username!!, picturePath)
            else -> view?.showAsDefault(username!!, picturePath)
        }
    }

    fun sendRequest() {
        view?.showLoading()
        FirebaseInteractor.sendRequest(uid!!, {
            User.sentRequests?.add(uid!!)
            Events.friendEvent(NotificationEvent(uid!!, "ACTION"))
            view?.hideLoading()
            view?.showAsRequestSent(username!!, picturePath)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun acceptRequest() {
        view?.showLoading()
        FirebaseInteractor.acceptRequest(uid!!, {
            User.requestAccepted(uid!!)
            Events.friendEvent(NotificationEvent(uid!!, "ACTION"))
            view?.hideLoading()
            view?.showAsFriend(username!!, picturePath)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun deleteRequest() {
        view?.showLoading()
        FirebaseInteractor.deleteRequest(uid, FirebaseInteractor.getUID(), {
            User.requestDeleted(uid!!)
            Events.friendEvent(NotificationEvent(uid!!, "ACTION"))
            view?.hideLoading()
            view?.showAsDefault(username!!, picturePath)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun cancelRequest() {
        view?.showLoading()
        FirebaseInteractor.deleteRequest(FirebaseInteractor.getUID(), uid, {
            User.requestCancelled(uid!!)
            Events.friendEvent(NotificationEvent(uid!!, "ACTION"))
            view?.hideLoading()
            view?.showAsDefault(username!!, picturePath)
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    fun deleteFriend() {
        view?.showLoading()
        FirebaseInteractor.deleteFriend(uid!!,
            {
                User.deleteFriend(uid!!)
                Events.friendEvent(NotificationEvent(uid!!, "ACTION"))
                view?.hideLoading()
                view?.showAsDefault(username!!, picturePath)
            }, {
                view?.hideLoading()
                view?.showError(it)
            })
    }
}