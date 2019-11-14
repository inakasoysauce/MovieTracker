package com.example.movieapplication.ui.profile.password_change

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor

class PasswordChangePresenter(view: PasswordChangeView) : BasePresenter<PasswordChangeView>(view) {

    fun changePassword(password: String) {
        FirebaseInteractor.changePassword(password, {
            view?.success()
        }, {
            view?.showError(it)
        })
    }
}