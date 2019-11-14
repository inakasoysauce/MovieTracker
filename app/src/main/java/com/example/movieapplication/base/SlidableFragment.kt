package com.example.movieapplication.base

import androidx.fragment.app.Fragment

interface SlidableFragment {
    fun getEnterAnimation() : Int
    fun getExitAnimation() : Int
    fun getFragment(): Fragment
}