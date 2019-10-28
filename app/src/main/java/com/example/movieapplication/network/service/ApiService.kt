package com.example.movieapplication.network.service

import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import com.example.movieapplication.network.model.SearchResult
import com.example.movieapplication.network.model.SimilarMovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/multi")
    suspend fun multiSearch(@Query("api_key") api_key: String, @Query("query") title: String) : Response<SearchResult>

    @GET("movie/{id}")
    fun getMovieByIdAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<Movie>>

    @GET("movie/{id}/credits")
    fun getMovieCastAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<MovieCast>>

    @GET("movie/{id}/similar")
    fun getSimilarMoviesAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SimilarMovieResponse>>

}