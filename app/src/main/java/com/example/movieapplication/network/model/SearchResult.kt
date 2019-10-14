package com.example.movieapplication.network.model

data class SearchResult(
    val page: Int,
    val results: ArrayList<SearchResultItem>,
    val total_pages: Int,
    val total_results: Int
)