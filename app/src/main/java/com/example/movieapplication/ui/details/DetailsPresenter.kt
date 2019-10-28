package com.example.movieapplication.ui.details

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import com.example.movieapplication.network.model.SimilarMovieResponse
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailsPresenter(private var view: IDetailsScreen?) : BasePresenter {

    @Inject
    lateinit var client: ApiClient

    @Inject
    lateinit var subscriptions : CompositeDisposable

    init {
        MovieTracker.movieComponent.inject(this)
    }

    suspend fun getMovieDetails(id : String?) {
        id?.let {
            try {
                val detailsResponse = client.getMovieDetailsAsync(it)
                val castResponse = client.getMovieCastAsync(it)
                val similarResponse = client.getSimilarMoviesAsync(it)
                handleResponses(detailsResponse.await(), castResponse.await(), similarResponse.await())
            } catch (e: Exception) {
                view?.showDetails(null)
            }
        }
    }

    private suspend fun handleResponses(detailsResponse: Response<Movie>, castResponse: Response<MovieCast>, similarResponse: Response<SimilarMovieResponse>) {
        if (detailsResponse.isSuccessful && castResponse.isSuccessful && similarResponse.isSuccessful) {
            withContext(Dispatchers.Main) {
                view?.showDetails(detailsResponse.body())
                view?.showCast(castResponse.body()?.cast)
                view?.showSimilarMovies(similarResponse.body()?.results)
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