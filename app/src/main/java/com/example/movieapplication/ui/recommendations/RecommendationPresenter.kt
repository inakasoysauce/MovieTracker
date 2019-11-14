package com.example.movieapplication.ui.recommendations

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.RecommendationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationPresenter(view: RecommendationView) : BasePresenter<RecommendationView>(view) {

    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getRecommendations() {
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            val response = FirebaseInteractor.getRecommendations()
            if (response.isSuccessful) {
                getMoviesAndSeries(response.body())
            } else {
                showError()
            }
        }
    }

    fun deleteRecommendation(friendID: String, id: String, type: String) {
        view?.showLoading()
        FirebaseInteractor.deleteRecommendation(friendID = friendID, id = id, type = type, success = {
            view?.hideLoading()
            view?.removed(friendID, id, type)
        }, error = {
            view?.hideLoading()
            view?.showError(it)
        })
    }

    private suspend fun getMoviesAndSeries(recommendations: ArrayList<RecommendationItem>?) {
        recommendations?.forEach {
            val response = client.getDetailsAsync(it.id!!, it.type!!).await()
            it.item = response.body()
        }
        withContext(Dispatchers.Main) {
            view?.hideLoading()
            view?.showRecommendations(recommendations)
        }
    }

    private suspend fun showError() {
        withContext(Dispatchers.Main) {
            view?.hideLoading()
            view?.showError("Couldn't load the info")
        }
    }
}