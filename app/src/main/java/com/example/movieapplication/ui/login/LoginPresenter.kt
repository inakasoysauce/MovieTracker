package com.example.movieapplication.ui.login

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor

class LoginPresenter(private var view : LoginView?) : BasePresenter {

    override fun destroyView() {
        view = null
    }

    fun attemptLogin(email: String, password: String) {
        FirebaseInteractor.login(email, password,{
            view?.success()
        },{
            view?.showError(it)
        })
    }

    fun attemptRegister(email: String, password: String, username: String) {
        FirebaseInteractor.createUser(email,password, username, {
            view?.success()
        }, {
            view?.showError(it)
        })
    }
}