package com.example.movieapplication.util

import com.example.movieapplication.base.BaseAdapterListener

interface PersonClickedListener : BaseAdapterListener {
    fun goToPersonDetails(id: String)
}