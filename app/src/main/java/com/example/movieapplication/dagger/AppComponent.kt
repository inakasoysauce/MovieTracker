package com.example.movieapplication.dagger

import com.example.movieapplication.ui.cast_list.CastListPresenter
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.details.DetailsPresenter
import com.example.movieapplication.ui.favourites.FavouritePresenter
import com.example.movieapplication.ui.friend_favourites.FriendFavouritePresenter
import com.example.movieapplication.ui.friends.FriendPresenter
import com.example.movieapplication.ui.login.LoginFragment
import com.example.movieapplication.ui.person_details.PersonDetailsPresenter
import com.example.movieapplication.ui.pictures.PicturesPresenter
import com.example.movieapplication.ui.recommendations.RecommendationPresenter
import com.example.movieapplication.ui.search.SearchActivity
import com.example.movieapplication.ui.search.SearchPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ServiceModule::class
])
interface AppComponent {
    fun inject(target: SearchPresenter)
    fun inject(target: DetailsPresenter)
    fun inject(target: SearchActivity)
    fun inject(target: DetailsActivity)
    fun inject(target: LoginFragment)
    fun inject(target: PersonDetailsPresenter)
    fun inject(target: CastListPresenter)
    fun inject(target: PicturesPresenter)
    fun inject(target: FavouritePresenter)
    fun inject(target: FriendFavouritePresenter)
    fun inject(target: RecommendationPresenter)
}