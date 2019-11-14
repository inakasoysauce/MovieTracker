package com.example.movieapplication.ui.profile

import android.graphics.Bitmap
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.user.User
import com.example.movieapplication.util.PictureTransforms
import java.io.ByteArrayOutputStream

class ProfilePresenter(view: ProfileView?) : BasePresenter<ProfileView>(view) {

    companion object {
        private const val PICTURE_WIDTH = 500F
    }

    fun uploadPicture(picture: Bitmap) {
        val baos = ByteArrayOutputStream()
        resizePicture(picture).compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        FirebaseInteractor.uploadPicture(data, {
            view?.success()
        }, {
            view?.showError(it)
        })
    }

    private fun resizePicture(bitmap: Bitmap) : Bitmap {
        val newHeight = PICTURE_WIDTH / bitmap.width * bitmap.height
        return PictureTransforms.resizePicture(bitmap, PICTURE_WIDTH, newHeight)
    }
}