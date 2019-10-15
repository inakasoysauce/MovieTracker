package com.example.movieapplication.network.service

import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import com.example.movieapplication.network.model.SearchResult
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("search/movie")
    suspend fun getMoviesByTitle(@Query("api_key") api_key: String, @Query("query") title: String) : Response<SearchResult>

    @GET("movie/{id}")
    fun getMovieByIdAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<Movie>>

    @GET("movie/{id}/credits")
    fun getMovieCastAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<MovieCast>>

}