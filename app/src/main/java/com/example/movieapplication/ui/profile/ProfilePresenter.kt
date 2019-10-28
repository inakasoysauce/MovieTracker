package com.example.movieapplication.ui.profile

import android.graphics.Bitmap
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import java.io.ByteArrayOutputStream

class ProfilePresenter(private var view: ProfileView?) : BasePresenter {


    fun uploadPicture() {
        val baos = ByteArrayOutputStream()
        User.profilePicture?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        FirebaseInteractor.uploadPicture(data, {
            view?.success()
        }, {
            view?.showError(it)
        })
    }

    override fun destroyView() {
        view = null
    }

}