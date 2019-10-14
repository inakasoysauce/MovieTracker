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

class MovieAdapter(listener: MovieClickedListener) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies : ArrayList<SearchResultItem>
    private val listener : MovieClickedListener

    interface MovieClickedListener {
        fun goToDetails(id: String)
        fun getCharSequence(viewID: Int, voteAverage: String, voteCount: String): CharSequence?
        fun getContext(): Context
    }

    init {
        movies = ArrayList()
        this.listener = listener
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val itemView = LayoutInflater
            .from(p0.context)
            .inflate(R.layout.search_result_item,p0,false)
        return MovieViewHolder(itemView, listener)

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val searchResultItem = movies[position]
        holder.titleTv.text = searchResultItem.title
        holder.releaseDateTv.text = searchResultItem.releaseDate
        holder.ratingTv.text = listener.getCharSequence(R.string.search_result_rating,searchResultItem.voteAverage.toString(),searchResultItem.voteCount.toString())
        holder.movieId =  searchResultItem.id.toString()
        GlideApp.with(listener.getContext())
            .load(imageUrl + searchResultItem.posterPath)
            .skipMemoryCache(false)
            .into(holder.posterIv)
    }

    fun addMovies(movies: ArrayList<SearchResultItem>?){
        movies?.let {
            this.movies = it
        }
        notifyDataSetChanged()
    }

    fun clear() {
        movies.clear()
        notifyDataSetChanged()
    }

    fun getMovies(): ArrayList<SearchResultItem> {
        return movies
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