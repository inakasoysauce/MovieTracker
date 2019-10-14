package com.example.movieapplication.dagger

import android.content.Context
import com.example.movieapplication.ui.details.DetailsPresenter
import com.example.movieapplication.ui.search.SearchPresenter
import dagger.Module
import dagger.Provides


@Module
class PresenterModule(private var context: Context) {

    @Provides
    fun provideSearchPresenter() : SearchPresenter {
        return SearchPresenter()
    }

    @Provides
    fun provideDetailsPresenter() : DetailsPresenter = DetailsPresenter()
}