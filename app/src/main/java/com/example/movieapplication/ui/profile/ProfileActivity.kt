package com.example.movieapplication.ui.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.user.User
import com.example.movieapplication.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity<ProfilePresenter>(), ProfileView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initComponents()
        setLoadingView(loading_screen)
    }

    override fun createPresenter(): ProfilePresenter {
        return ProfilePresenter(this)
    }

    override fun initComponents() {
        profile_img.setOnClickListener {
            pickImageFromGallery()
        }
        setProfilePicture()
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == Constants.PICK_IMAGE) {
            if (data != null) {
                handlePictureIntent(data)
                setProfilePicture()
                showMainLoading()
                presenter?.uploadPicture()
            }
        }
    }

    private fun handlePictureIntent(data: Intent) {
        val uri = data.data
        val inputStream = this.contentResolver.openInputStream(uri!!)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        User.profilePicture = bitmap
    }

    private fun setProfilePicture() {
        if (User.profilePicture != null) {
            profile_img.setImageBitmap(User.profilePicture)
        }
    }

    override fun success() {
        hideMainLoading()
    }

    override fun showError(message: String) {
        hideMainLoading()
        Snackbar.make(main_layout, message, Snackbar.LENGTH_LONG).show()
    }
}
