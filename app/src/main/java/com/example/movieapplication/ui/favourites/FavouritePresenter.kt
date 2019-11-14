package com.example.movieapplication.ui.favourites

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.user.User
import kotlinx.coroutines.*
import retrofit2.Response

class FavouritePresenter(view: FavouriteView) : BasePresenter<FavouriteView>(view) {


    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getFavourites(mediaType: String) {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            val calls = ArrayList<Deferred<Response<SearchResultItem>>>()
            val list = if (mediaType == SearchResultItem.person) {
                User.getFavouritePersons()
            } else
                User.getFavouriteMoviesAndSeries()
            list.forEach { item ->
                val id = item.id
                val type = item.type
                if (id != null && type != null) {
                    calls.add(client.getFavouriteAsync(id, type))
                }
            }
            val responses = ArrayList<Response<SearchResultItem>>()
            calls.forEach {
                responses.add(it.await())
            }
            handleResponses(responses)
        }
    }

    fun removeFromFavourites(id: String?, type: String?) {
        if (id != null && type != null) {
            view?.showLoading()
            FirebaseInteractor.removeFromFavourites(id, type, {
                User.removeFromFavourite(id, type)
                view?.hideLoading()
                view?.itemRemoved(id, type)
            }, {
                view?.hideLoading()
                view?.showError(it)
            })
        }
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
}