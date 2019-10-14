package com.example.movieapplication.dagger

import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.details.DetailsPresenter
import com.example.movieapplication.ui.search.MainActivity
import com.example.movieapplication.ui.search.SearchPresenter
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    PresenterModule::class,
    ServiceModule::class
])
interface AppComponent {
    fun inject(target: SearchPresenter)
    fun inject(target: DetailsPresenter)
    fun inject(target: MainActivity)
    fun inject(target: DetailsActivity)
}