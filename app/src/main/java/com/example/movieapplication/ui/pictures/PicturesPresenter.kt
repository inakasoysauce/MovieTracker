package com.example.movieapplication.ui.pictures

import com.example.movieapplication.MovieTracker
import com.example.movieapplication.base.BasePresenter
import com.example.movieapplication.network.model.PictureResponse
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.util.ConfigInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PicturesPresenter(view: PicturesView) : BasePresenter<PicturesView>(view) {

    init {
        MovieTracker.movieComponent.inject(this)
    }

    fun getPictures(id: String?, type: String?) {
        if (id != null && type != null) {
            GlobalScope.launch(Dispatchers.IO) {
                val response = client.getPictures(id, type)
                if (response.isSuccessful && response.body() != null) {
                    getPicturesList(type, response.body()!!)
                } else {
                    view?.showError("Something went wrong")
                }
            }
        }
    }

    private suspend fun getPicturesList(type: String, response: PictureResponse) {
        val list = ArrayList<String>()
        when (type) {
            SearchResultItem.movie, SearchResultItem.tvShow -> {
                for (item in response.backdrops!!) {
                    list.add("${ConfigInfo.normalImageUrl}${item.file_path!!}")
                }
                for (item in response.posters!!) {
                    list.add("${ConfigInfo.normalImageUrl}${item.file_path!!}")
                }
            }
            else -> {
                for (item in response.results!!) {
                    list.add("${ConfigInfo.normalImageUrl}${item.file_path!!}")
                }
            }
        }
        withContext(Dispatchers.Main) {
            view?.loadImages(list)
        }
    }

}