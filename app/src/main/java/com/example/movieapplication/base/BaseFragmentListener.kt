package com.example.movieapplication.base

import androidx.fragment.app.Fragment

interface BaseFragmentListener : BaseView {

    fun showMainLoading()
    fun hideMainLoading()
    fun hideContainedFragment(fragment: SlidableFragment)
}