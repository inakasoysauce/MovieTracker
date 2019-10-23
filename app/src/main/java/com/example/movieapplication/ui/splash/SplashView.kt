package com.example.movieapplication.ui.splash

import com.example.movieapplication.base.BaseView

interface SplashView : BaseView {
    fun goToMainScreen()
    fun showErrorMessage(message: String)
}