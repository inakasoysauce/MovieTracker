package com.example.movieapplication.ui.details.rating

import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.RatingItem
import com.example.movieapplication.user.User

class RatingPresenter(view: RatingView) : BasePresenter<RatingView>(view) {


    fun giveRating(id: String?, type: String?, rating: Int) {
        if (id != null && type != null) {
            view?.showLoading()
            FirebaseInteractor.giveRating(id, type, rating, {
                addRatingToUser(id, type, rating)
                view?.hideLoading()
                view?.success(rating)
            }, {
                view?.hideLoading()
                view?.showError(it)
            })
        }
    }

    private fun addRatingToUser(id: String, type: String, rating: Int) {
        val ratingItem = RatingItem()
        ratingItem.id = id
        ratingItem.type = type
        ratingItem.rating = rating
        User.ratingList?.add(ratingItem)
    }
}