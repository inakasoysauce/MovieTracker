package com.example.movieapplication.ui.search

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SearchPresenter(view: SearchView?) : BasePresenter<SearchView>(view) {


    @Inject
    lateinit var subscriptions: CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }


    suspend fun getSearchResults(title: String) {
        try {
            val response = client.multiSearch(title)
            if (response.isSuccessful) {
                val result = response.body()
                result?.let {
                    if (it.total_results > 0) {
                        view?.addSearchResults(it.results)
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
        super.destroyView()
    }
}