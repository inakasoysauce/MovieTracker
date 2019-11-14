package com.example.movieapplication.util

import com.example.movieapplication.base.BaseAdapterListener

interface PictureClickedListener : BaseAdapterListener {

    fun showPicture(position: Int)
}