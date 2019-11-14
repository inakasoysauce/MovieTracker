package com.example.movieapplication.base

interface BaseView {
    fun initComponents() {}
    fun showError(message: String)
    fun showLoading()
    fun hideLoading()
}