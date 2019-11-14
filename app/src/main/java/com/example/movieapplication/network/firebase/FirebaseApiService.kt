package com.example.movieapplication.network.firebase

import com.example.movieapplication.network.model.FriendItem
import com.example.movieapplication.network.model.RecommendationItem
import com.example.movieapplication.network.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FirebaseApiService {

    @GET("getUserInfo")
    suspend fun getUserInfo(@Query("userid") userid: String?) : Response<UserModel>

    @GET("getAllUsers")
    suspend fun getAllUser(@Query("uid") userid: String?) : Response<ArrayList<FriendItem>>

    @GET("getFriends")
    suspend fun getFriends(@Query("uid") userid: String?) : Response<ArrayList<FriendItem>>

    @GET("getReceivedRequests")
    suspend fun getReceivedRequests(@Query("uid") userid: String?) : Response<ArrayList<FriendItem>>

    @GET("getRecommendations")
    suspend fun getRecommendations(@Query("uid") userid: String?) : Response<ArrayList<RecommendationItem>>


}