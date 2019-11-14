package com.example.movieapplication.network.service

import com.example.movieapplication.network.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/multi")
    suspend fun multiSearch(@Query("api_key") api_key: String, @Query("query") title: String) : Response<SearchResult>

    @GET("movie/{id}")
    fun getMovieByIdAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<MovieOrSeries>>

    @GET("tv/{id}")
    fun getTvShowByIdAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<MovieOrSeries>>

    @GET("movie/{id}/credits")
    fun getMovieCastAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<CastResponse>>

    @GET("tv/{id}/credits")
    fun getTVShowCastAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<CastResponse>>

    @GET("movie/{id}/similar")
    fun getSimilarMoviesAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SimilarListResponse>>

    @GET("tv/{id}/similar")
    fun getSimilarTVShowAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SimilarListResponse>>

    @GET("person/{id}")
    fun getPersonDetailsAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<Person>>

    @GET("person/{id}/combined_credits")
    fun getPersonCreditsAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<PersonCredit>>

    @GET("movie/{id}/images")
    suspend fun getMoviePictures(@Path("id") id: String, @Query("api_key") api_key: String) : Response<PictureResponse>

    @GET("tv/{id}/images")
    suspend fun getSeriesPictures(@Path("id") id: String, @Query("api_key") api_key: String) : Response<PictureResponse>

    @GET("person/{id}/tagged_images")
    suspend fun getPersonPictures(@Path("id") id: String, @Query("api_key") api_key: String) : Response<PictureResponse>

    @GET("movie/{id}")
    fun getFavouriteMovieAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SearchResultItem>>

    @GET("tv/{id}")
    fun getFavouriteSeriesAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SearchResultItem>>

    @GET("person/{id}")
    fun getFavouritePersonAsync(@Path("id") id: String, @Query("api_key") api_key: String) : Deferred<Response<SearchResultItem>>




}