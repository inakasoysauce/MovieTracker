package com.example.movieapplication.ui.pictures

import com.example.movieapplication.base.BaseView

interface PicturesView : BaseView {

    fun loadImages(picturesList: ArrayList<String>?)

}