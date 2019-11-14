package com.example.movieapplication.ui.cast_list

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.client.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CastListPresenter(view: CastListView) : BasePresenter<CastListView>(view) {

    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getCastList(id: String?, type: String?) {
        if (id != null && type != null)
            GlobalScope.launch(Dispatchers.IO) {
                val response = client.getCastListAsync(id, type).await()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        view?.showCastList(response.body()?.cast)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        view?.showError("Something went wrong")
                    }
                }
            }
    }
}