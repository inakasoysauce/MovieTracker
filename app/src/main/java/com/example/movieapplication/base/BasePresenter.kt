package com.example.movieapplication.base

interface BasePresenter<T> {
    fun addView(view: T)
    fun destroyView()
}