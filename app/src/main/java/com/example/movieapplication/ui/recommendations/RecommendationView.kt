package com.example.movieapplication.ui.recommendations

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.RecommendationItem

interface RecommendationView : BaseView {

    fun showRecommendations(list: ArrayList<RecommendationItem>?)
    fun removed(friendID: String, id: String, type: String)
}