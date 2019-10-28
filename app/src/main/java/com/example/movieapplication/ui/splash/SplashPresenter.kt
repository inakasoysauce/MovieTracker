package com.example.movieapplication.ui.splash

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor

class SplashPresenter(private var view: SplashView?) : BasePresenter {

    fun tryAutoLogin() {
        if (FirebaseInteractor.loggedIn()) {
            var responseCount = 0
            FirebaseInteractor.getUserData({
                if (++responseCount == 2)
                    view?.goToMainScreen()
            }, {
                view?.showErrorMessage(it)
                if (++responseCount == 2)
                    view?.goToMainScreen()
            })
        } else {
            view?.goToMainScreen()
        }
    }

    override fun destroyView() {
        view = null
    }
}