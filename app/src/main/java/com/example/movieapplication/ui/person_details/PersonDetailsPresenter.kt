package com.example.movieapplication.ui.person_details

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.*
import com.example.movieapplication.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class PersonDetailsPresenter(view: PersonDetailsView) : BasePresenter<PersonDetailsView>(view) {

    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getPersonDetails(id: String?) {
        if (id != null) {
            GlobalScope.launch(Dispatchers.IO) {
                val personResponse = client.getPersonDetailsAsync(id)
                val creditResponse = client.getPersonCreditAsync(id)
                handleResponses(personResponse.await(), creditResponse.await())
            }
        }
    }

    private suspend fun handleResponses(personResponse: Response<Person>, creditResponse: Response<PersonCredit>) {
        if (personResponse.isSuccessful && creditResponse.isSuccessful) {
            withContext(Dispatchers.Main) {
                view?.showDetails(personResponse.body())
                view?.showCredits(creditResponse.body()?.cast)
            }
        } else {
            withContext(Dispatchers.Main) {
                view?.showDetails(null)
            }
        }
    }

    fun addToFavourites(id: String?) {
        val type = SearchResultItem.person
        if (id != null) {
            view?.showLoading()
            FirebaseInteractor.addToFavourites(id, type, {
                addFavouriteToUser(id)
                view?.addedToFavourites()
                view?.hideLoading()
            }, {
                view?.showError(it)
                view?.hideLoading()
            })
        }
    }

    private fun addFavouriteToUser(id: String) {
        val item = FavouriteItem()
        item.id = id
        item.type = SearchResultItem.person
        User.favouriteList?.add(item)
    }


}