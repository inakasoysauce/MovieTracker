package com.example.movieapplication.base

interface BasePresenter<T : BaseView> {
    fun addView(view: T)
    fun destroyView()
}