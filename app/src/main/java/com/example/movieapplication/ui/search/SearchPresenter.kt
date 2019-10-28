package com.example.movieapplication.ui.search

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchPresenter(private var view: ISearchScreen?) : BasePresenter {


    @Inject
    lateinit var client: ApiClient

    @Inject
    lateinit var subscriptions: CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }


    suspend fun getMovies(title: String) {
        try {
            val response = client.multiSearch(title)
            if (response.isSuccessful) {
                val result = response.body()
                result?.let {
                    if (it.total_results > 0) {
                        view?.addMovies(it.results)
                    } else {
                        view?.noResult()
                    }
                }
            } else {
                view?.showNoConnectionMessage()
            }
        } catch (e: Exception) {
            view?.showNoConnectionMessage()
        }
    }

    override fun destroyView() {
        subscriptions.clear()
        view = null
    }
}