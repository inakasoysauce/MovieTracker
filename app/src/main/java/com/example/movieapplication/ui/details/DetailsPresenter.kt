package com.example.movieapplication.ui.details

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailsPresenter : BasePresenter<IDetailsScreen> {

    private var view: IDetailsScreen? = null

    @Inject
    lateinit var client: ApiClient

    @Inject
    lateinit var subscriptions : CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }

    override fun addView(view: IDetailsScreen) {
        this.view = view
    }

    suspend fun getMovieDetails(id : String?) {
        id?.let {
            try {
                val detailsResponse = client.getMovieDetailsAsync(it)
                val castResponse = client.getMovieCastAsync(it)
                handleResponses(detailsResponse.await(), castResponse.await())
            } catch (e: Exception) {
                view?.showDetails(null)
            }
        }
    }

    private suspend fun handleResponses(detailsResponse: Response<Movie>, castResponse: Response<MovieCast>) {
        if (detailsResponse.isSuccessful && castResponse.isSuccessful) {
            withContext(Dispatchers.Main) {
                view?.showDetails(detailsResponse.body())
                view?.showCast(castResponse.body()?.cast)
            }
        } else {
            withContext(Dispatchers.Main){
                view?.showDetails(null)
            }
        }
    }

    override fun destroyView() {
        view = null
        subscriptions.clear()
    }
}