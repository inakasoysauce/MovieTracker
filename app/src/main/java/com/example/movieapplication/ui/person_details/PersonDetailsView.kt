package com.example.movieapplication.ui.person_details

import com.example.movieapplication.base.BaseView
import com.example.movieapplication.network.model.Person
import com.example.movieapplication.network.model.PersonCredit

interface PersonDetailsView : BaseView {

    fun showDetails(person: Person?)
    fun showCredits(credits : ArrayList<PersonCredit.Credit>?)
    fun addedToFavourites()
}