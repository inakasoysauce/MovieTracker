package com.example.movieapplication.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.util.ConfigInfo.imageUrl
import com.example.movieapplication.util.GlideApp
import kotlinx.android.synthetic.main.search_result_item.view.*

class MovieAdapter(private val listener: MovieClickedListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var searchResults : ArrayList<SearchResultItem> = ArrayList()

    interface MovieClickedListener {
        fun goToDetails(id: String)
        fun getCharSequence(viewID: Int, voteAverage: String, voteCount: String): CharSequence?
        fun getContext(): Context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val itemView = LayoutInflater
            .from(p0.context)
            .inflate(R.layout.search_result_item,p0,false)
        return MovieViewHolder(itemView, listener)

    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val searchResultItem = searchResults[position]
        holder.titleTv.text = searchResultItem.title
        holder.releaseDateTv.text = searchResultItem.getFirstInfo()
        holder.ratingTv.text = searchResultItem.getSecondInfo(listener.getContext())
        holder.movieId =  searchResultItem.id.toString()
        GlideApp.with(listener.getContext())
            .load(imageUrl + searchResultItem.posterPath)
            .skipMemoryCache(false)
            .into(holder.posterIv)
    }

    fun addMovies(movies: ArrayList<SearchResultItem>?){
        movies?.let {
            this.searchResults.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun clear() {
        searchResults.clear()
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View, listener: MovieClickedListener) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.title_text_view
        val releaseDateTv: TextView = itemView.release_date_text_view
        val ratingTv: TextView = itemView.rating_text_view
        val posterIv: ImageView = itemView.poster_image
        var movieId : String? = null
        init {
            itemView.setOnClickListener {
                movieId?.let {
                    listener.goToDetails(it)
                }
            }
        }
    }
}