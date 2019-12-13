package com.example.movieapplication.network.client

import com.example.movieapplication.network.model.*
import com.example.movieapplication.util.ConfigInfo.API_KEY
import com.example.movieapplication.util.ConfigInfo.BASE_URL
import com.example.movieapplication.network.service.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: ApiService

    init {
        service = retrofit.create(ApiService::class.java)
    }

    suspend fun multiSearch(title: String): Response<SearchResult> =
        service.multiSearch(API_KEY, title)

    fun getDetailsAsync(id: String, type: String): Deferred<Response<MovieOrSeries>> {
        return if (type == SearchResultItem.movie) {
            getMovieDetailsAsync(id)
        } else {
            getTVShowDetailsAsync(id)
        }
    }

    fun getCastListAsync(id: String, type: String): Deferred<Response<CastResponse>> {
        return if (type == SearchResultItem.movie) {
            getMovieCastAsync(id)
        } else {
            getTVShowCastAsync(id)
        }
    }

    fun getFavouriteAsync(id: String, type: String) : Deferred<Response<SearchResultItem>> {
        return when(type) {
            SearchResultItem.movie -> getFavouriteMovieAsync(id)
            SearchResultItem.tvShow -> getFavouriteSeriesAsync(id)
            SearchResultItem.person ->getFavouritePersonAsync(id)
            else -> getFavouriteMovieAsync(id)
        }
    }

    suspend fun getPictures(id: String, type: String): Response<PictureResponse> {
        return when (type) {
            SearchResultItem.movie -> service.getMoviePictures(id, API_KEY)
            SearchResultItem.tvShow -> service.getSeriesPictures(id, API_KEY)
            SearchResultItem.person -> service.getPersonPictures(id, API_KEY)
            else -> service.getMoviePictures(id, API_KEY)
        }
    }

    fun getSimilarAsync(id: String, type: String): Deferred<Response<SimilarListResponse>> {
        return if (type == SearchResultItem.movie) {
            getSimilarMoviesAsync(id)
        } else {
            getSimilarTVShowAsync(id)
        }
    }

    fun getPersonDetailsAsync(id: String) = service.getPersonDetailsAsync(id, API_KEY)

    fun getPersonCreditAsync(id: String) = service.getPersonCreditsAsync(id, API_KEY)

    private fun getMovieDetailsAsync(id: String): Deferred<Response<MovieOrSeries>> =
        service.getMovieByIdAsync(id, API_KEY)

    private fun getTVShowDetailsAsync(id: String): Deferred<Response<MovieOrSeries>> =
        service.getTvShowByIdAsync(id, API_KEY)

    private fun getMovieCastAsync(id: String): Deferred<Response<CastResponse>> =
        service.getMovieCastAsync(id, API_KEY)

    private fun getTVShowCastAsync(id: String): Deferred<Response<CastResponse>> =
        service.getTVShowCastAsync(id, API_KEY)

    private fun getSimilarMoviesAsync(id: String): Deferred<Response<SimilarListResponse>> =
        service.getSimilarMoviesAsync(id, API_KEY)

    private fun getSimilarTVShowAsync(id: String): Deferred<Response<SimilarListResponse>> =
        service.getSimilarTVShowAsync(id, API_KEY)

    private fun getFavouriteMovieAsync(id: String) = service.getFavouriteMovieAsync(id, API_KEY)

    private fun getFavouriteSeriesAsync(id: String) = service.getFavouriteSeriesAsync(id, API_KEY)

    private fun getFavouritePersonAsync(id: String) = service.getFavouritePersonAsync(id, API_KEY)
}