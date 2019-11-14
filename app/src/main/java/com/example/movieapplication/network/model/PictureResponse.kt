package com.example.movieapplication.network.model

class PictureResponse {
    val backdrops: ArrayList<PictureItem>? = null
    val posters: ArrayList<PictureItem>? = null
    val results: ArrayList<PictureItem>? = null
}

class PictureItem {
    val file_path: String? = null
}