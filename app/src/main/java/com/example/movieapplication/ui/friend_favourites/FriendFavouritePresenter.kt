package com.example.movieapplication.ui.friend_favourites

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.SearchResultItem
import kotlinx.coroutines.*
import retrofit2.Response

class FriendFavouritePresenter(view: FriendFavouriteView) : BasePresenter<FriendFavouriteView>(view) {

    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getFavourites(mediaType: String, uid: String) {
        view?.showLoading()
        FirebaseInteractor.getFavouritesTo(uid, { favourites ->
            GlobalScope.launch(Dispatchers.IO) {
                val calls = ArrayList<Deferred<Response<SearchResultItem>>>()
                val list = if (mediaType == SearchResultItem.person)
                    getPersons(favourites) else getMoviesAndSeries(favourites)
                list.forEach {
                    val id = it["id"]!!
                    val type = it["type"]!!
                    calls.add(client.getFavouriteAsync(id, type))
                }
                val responses = ArrayList<Response<SearchResultItem>>()
                calls.forEach {
                    responses.add(it.await())
                }
                handleResponses(responses)
            }
        }, {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    private suspend fun handleResponses(responses: ArrayList<Response<SearchResultItem>>) {
        val list = ArrayList<SearchResultItem>()
        responses.forEach { response ->
            if (response.isSuccessful) {
                list.add(response.body()!!)
            }
        }
        withContext(Dispatchers.Main) {
            view?.hideLoading()
            view?.showFavourites(list)
        }
    }

    private fun getMoviesAndSeries(list: ArrayList<HashMap<String, String>>): ArrayList<HashMap<String, String>> {
        val result = ArrayList<HashMap<String, String>>()
        list.forEach {
            if (it["type"] != SearchResultItem.person) {
                result.add(it)
            }
        }
        return result
    }

    private fun getPersons(list: ArrayList<HashMap<String, String>>): ArrayList<HashMap<String, String>> {
        val result = ArrayList<HashMap<String, String>>()
        list.forEach {
            if (it["type"] == SearchResultItem.person) {
                result.add(it)
            }
        }
        return result
    }
}