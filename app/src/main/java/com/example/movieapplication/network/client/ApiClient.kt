package com.example.movieapplication.network.client

import com.example.movieapplication.util.ConfigInfo.API_KEY
import com.example.movieapplication.util.ConfigInfo.BASE_URL
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import com.example.movieapplication.network.model.SearchResult
import com.example.movieapplication.network.service.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var service: ApiService

    init {
        service = retrofit.create(ApiService::class.java)
    }

    suspend fun getMovieList(title: String) : Response<SearchResult> = service.getMoviesByTitle(API_KEY,title)

    fun getMovieDetailsAsync(id: String) : Deferred<Response<Movie>> =  service.getMovieByIdAsync(id,API_KEY)

    fun getMovieCastAsync(id: String) : Deferred<Response<MovieCast>> = service.getMovieCastAsync(id,API_KEY)
}