package com.example.movieapplication.ui.splash

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor

class SplashPresenter : BasePresenter<SplashView> {

    private var view : SplashView? = null

    override fun addView(view: SplashView) {
        this.view = view
    }

    fun tryAutoLogin() {
        if (FirebaseInteractor.loggedIn()){
            FirebaseInteractor.autoLogin({
                view?.goToMainScreen()
            }, {
                view?.showErrorMessage(it)
            })
        } else {
            view?.goToMainScreen()
        }
    }

    override fun destroyView() {
        view = null
    }
}