package com.example.movieapplication.base

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.network.client.ApiClient
import javax.inject.Inject

abstract class BasePresenter<P: BaseView?>(protected var view: P?) {

    @Inject
    protected lateinit var client: ApiClient

    open fun destroyView() {
        view = null
    }
}