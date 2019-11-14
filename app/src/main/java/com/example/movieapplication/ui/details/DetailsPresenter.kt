package com.example.movieapplication.ui.details

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.FavouriteItem
import com.example.movieapplication.network.model.MovieOrSeries
import com.example.movieapplication.network.model.SimilarListResponse
import com.example.movieapplication.user.User
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailsPresenter(view: DetailsView?) : BasePresenter<DetailsView>(view) {

    @Inject
    lateinit var subscriptions: CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }

    suspend fun getDetails(id: String?, type: String?) {
        if (id != null && type != null) {
            try {
                val detailsResponse = client.getDetailsAsync(id, type)
                val similarResponse = client.getSimilarAsync(id, type)
                handleResponses(detailsResponse.await(), similarResponse.await())
            } catch (e: Exception) {
                view?.showDetails(null)
            }
        }
    }

    fun addToFavourites(id: String?, type: String?) {
        if (id != null && type != null) {
            view?.showLoading()
            FirebaseInteractor.addToFavourites(id, type, {
                addFavouriteToUser(id, type)
                view?.addedToFavourites()
                view?.hideLoading()
            }, {
                view?.showError(it)
                view?.hideLoading()
            })
        }
    }

    private fun addFavouriteToUser(id: String, type: String) {
        val item = FavouriteItem()
        item.id = id
        item.type = type
        User.favouriteList?.add(item)
    }

    fun deleteRating(id: String?, type: String?) {
        if (id != null && type != null) {
            view?.showLoading()
            FirebaseInteractor.deleteRating(id, type, {
                view?.hideLoading()
                view?.rateDeleted()
            }, {
                view?.hideLoading()
                view?.showError(it)
            })
        }
    }

    fun recommend(friendID: String, id: String?, type: String?) {
        if (id != null && type != null) {
            view?.showLoading()
            FirebaseInteractor.recommend(friendID, id, type, {
                view?.hideLoading()
                view?.showError("Recommendation sent!")
            }, {
                view?.hideLoading()
                view?.showError(it)
            })
        }
    }

    private suspend fun handleResponses(detailsResponse: Response<MovieOrSeries>, similarResponse: Response<SimilarListResponse>) {
        if (detailsResponse.isSuccessful && similarResponse.isSuccessful) {
            withContext(Dispatchers.Main) {
                view?.showDetails(detailsResponse.body())
                view?.showSimilarMoviesOrSeries(similarResponse.body()?.results)
            }
        } else {
            withContext(Dispatchers.Main) {
                view?.showDetails(null)
            }
        }
    }

    override fun destroyView() {
        subscriptions.clear()
        super.destroyView()
    }
}