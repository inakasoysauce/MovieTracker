package com.example.movieapplication.ui.details.rating

import com.example.movieapplication.base.BaseView

interface RatingView : BaseView{
    fun success(rating: Int)
}