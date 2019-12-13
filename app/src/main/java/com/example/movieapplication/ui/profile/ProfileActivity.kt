package com.example.movieapplication.ui.profile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.base.SlidableFragment
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.ui.favourites.FavouriteFragment
import com.example.movieapplication.ui.friend_action.FriendActionFragment
import com.example.movieapplication.ui.friend_favourites.FriendFavouriteFragment
import com.example.movieapplication.ui.friends.FriendFragment
import com.example.movieapplication.ui.profile.password_change.PasswordChangeFragment
import com.example.movieapplication.user.User
import com.example.movieapplication.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity<ProfilePresenter, ProfileView>(), ProfileView, FriendFragment.FriendFragmentListener, FriendActionFragment.FriendActionFragmentListener {

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
        btn_change_password.setOnClickListener {
            showPasswordChangeFragment()
        }
        btn_see_favourite_movies.setOnClickListener {
            showFavouriteMovies()
        }
        btn_see_favourite_persons.setOnClickListener {
            showFavouritePersons()
        }
        btn_see_friends.setOnClickListener {
            showFriends()
        }
        tv_username.text = User.username
        profile_img.setImageDrawable(getDrawable(R.drawable.dummy_profile_picture))
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
        if (requestCode == Constants.PICK_IMAGE) {
            if (data != null) {
                val bitmap = handlePictureIntent(data)
                showMainLoading()
                presenter?.uploadPicture(bitmap)
            }
        }
    }

    private fun handlePictureIntent(data: Intent): Bitmap {
        val uri = data.data
        val inputStream = this.contentResolver.openInputStream(uri!!)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun setProfilePicture() {
        Picasso.get()
            .load(User.picture_path)
            .placeholder(R.drawable.dummy_profile_picture)
            .into(profile_img)
    }

    private fun showPasswordChangeFragment() {
        showFragment(PasswordChangeFragment(), R.id.fragment_container)
    }

    private fun showFavouriteMovies() {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.movie)
        showFavourites(bundle)
    }

    private fun showFavouritePersons() {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.person)
        showFavourites(bundle)
    }

    override fun showFavouriteMoviesFriend(uid: String, username: String) {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.movie)
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        showFragment(FriendFavouriteFragment(), R.id.fragment_container, {}, bundle)
    }

    override fun showFavouritePersonsFriend(uid: String, username: String) {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.person)
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        showFragment(FriendFavouriteFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun showFriends() {
        val bundle = Bundle()
        bundle.putString("type", Constants.FRIENDS)
        showFragment(FriendFragment(), R.id.fragment_container,{}, bundle)
    }

    private fun showFavourites(bundle: Bundle) {
        showFragment(FavouriteFragment(), R.id.fragment_container, {}, bundle)
    }

    override fun showUser(uid: String, username: String, picturePath: String?) {
        val bundle = Bundle()
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        bundle.putString("picture_path", picturePath)
        showFragment(FriendActionFragment(), R.id.fragment_container, {}, bundle)
    }

    override fun hideContainedFragment(fragment: SlidableFragment) {
        hideFragment(fragment)
    }

    override fun success() {
        setProfilePicture()
        hideMainLoading()
    }

    override fun showError(message: String) {
        hideMainLoading()
        Snackbar.make(main_layout, message, Snackbar.LENGTH_LONG).show()
    }
}
