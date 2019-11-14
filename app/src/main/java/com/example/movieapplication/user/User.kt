package com.example.movieapplication.user

import com.example.movieapplication.network.model.*
import kotlin.concurrent.fixedRateTimer

object User {
    var username: String? = null
        set(value) {
            field = value
            loggedIn = field != null
        }
    var loggedIn: Boolean = false

    var picture_path: String? = null
    var favouriteList: ArrayList<FavouriteItem>? = null
    var ratingList: ArrayList<RatingItem>? = null
    var friendList: ArrayList<String>? = null
    var incomingRequests: ArrayList<String>? = null
    var sentRequests: ArrayList<String>? = null

    fun getFavouriteMoviesAndSeries(): ArrayList<FavouriteItem> {
        val masList = ArrayList<FavouriteItem>()
        favouriteList?.forEach {
            if (it.type != SearchResultItem.person) {
                masList.add(it)
            }
        }
        return masList
    }

    fun getFavouritePersons(): ArrayList<FavouriteItem> {
        val personList = ArrayList<FavouriteItem>()
        favouriteList?.forEach {
            if (it.type == SearchResultItem.person) {
                personList.add(it)
            }
        }
        return personList
    }

    fun removeFromFavourite(id: String, type: String) {
        val item = favouriteList?.find {
            it.id == id && it.type == type
        }
        favouriteList?.remove(item)
    }

    fun isFavourite(movieOrSeries: MovieOrSeries): Boolean {
        favouriteList?.forEach {
            if (it.id == movieOrSeries.id.toString() && it.type == movieOrSeries.type) {
                return true
            }
        }
        return false
    }

    fun isFavourite(person: Person): Boolean {
        favouriteList?.forEach {
            if (it.id == person.id.toString() && it.type == SearchResultItem.person) {
                return true
            }
        }
        return false
    }

    fun getRate(movieOrSeries: MovieOrSeries): Int {
        var rating = -1
        ratingList?.forEach {
            if (it.id == movieOrSeries.id.toString() && it.type == movieOrSeries.type) {
                rating = it.rating!!
            }
        }
        return rating
    }

    fun addFriendsAll(list: ArrayList<FriendItem>?) {
        list?.let {
            friendList?.clear()
            it.forEach { item ->
                friendList?.add(item.uid!!)
            }
        }
    }

    fun addReceivedRequestsAll(list: ArrayList<FriendItem>?) {
        list?.let {
            incomingRequests?.clear()
            it.forEach { item ->
                incomingRequests?.add(item.uid!!)
            }
        }
    }

    fun removeRating(id: String, type: String) {
        val item = ratingList?.find {
            it.id == id && it.type == type
        }
        ratingList?.remove(item)
    }

    fun requestAccepted(user: String) {
        incomingRequests?.remove(user)
        friendList?.add(user)
    }

    fun requestDeleted(user: String) {
        incomingRequests?.remove(user)
    }

    fun requestCancelled(user: String) {
        sentRequests?.remove(user)
    }

    fun alreadyFriend(user: String): Boolean {
        return friendList?.contains(user) ?: false
    }

    fun alreadySentRequest(user: String): Boolean {
        return sentRequests?.contains(user) ?: false
    }

    fun alreadyRequestReceived(user: String): Boolean {
        return incomingRequests?.contains(user) ?: false
    }

    fun addToIncomingRequests(user: String) {
        if (!alreadyRequestReceived(user)) {
            incomingRequests?.add(user)
        }
    }

    fun moveToFriends(user: String) {
        friendList?.add(user)
        sentRequests?.remove(user)
    }

    fun deleteFriend(user: String) {
        friendList?.remove(user)
    }
}