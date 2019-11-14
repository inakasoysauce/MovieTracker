package com.example.movieapplication.util

import com.example.movieapplication.base.BaseAdapterListener

interface MovieOrSeriesClickedListener : BaseAdapterListener {
    fun goToDetails(id: String, type: String)
}