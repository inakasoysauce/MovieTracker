package com.example.movieapplication.ui.login

import android.provider.ContactsContract
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.google.firebase.auth.FirebaseUser

class LoginPresenter : BasePresenter<LoginView> {

    private var view : LoginView? = null

    override fun addView(view: LoginView) {
        this.view = view
    }

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