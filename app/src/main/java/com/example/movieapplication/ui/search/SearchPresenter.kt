package com.example.movieapplication.ui.search

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchPresenter : BasePresenter<ISearchScreen> {

    private var view: ISearchScreen? = null

    @Inject
    lateinit var client: ApiClient

    @Inject
    lateinit var subscriptions: CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }


    override fun addView(view: ISearchScreen) {
        this.view = view
        subscribeToTopic()
    }


    private fun subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("my_topic")
    }

    suspend fun getMovies(title: String) {
        try {
            val response = client.getMovieList(title)
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