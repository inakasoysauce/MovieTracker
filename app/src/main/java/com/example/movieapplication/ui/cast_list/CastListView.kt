package com.example.movieapplication.ui.cast_list

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.CastResponse

interface CastListView : BaseView {
    fun showCastList(list: ArrayList<CastResponse.Cast>?)
}