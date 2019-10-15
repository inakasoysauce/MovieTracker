package com.example.movieapplication.dagger

import com.example.movieapplication.ui.details.DetailsPresenter
import com.example.movieapplication.ui.search.SearchPresenter
import dagger.Module
import dagger.Provides


@Module
class PresenterModule {

    @Provides
    fun provideSearchPresenter() : SearchPresenter {
        return SearchPresenter()
    }

    @Provides
    fun provideDetailsPresenter() : DetailsPresenter = DetailsPresenter()
}