package com.example.movieapplication.ui.details.adapter

import android.animation.LayoutTransition
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.GlideApp

class SimilarMovieAdapter(private val context: Context) : RecyclerView.Adapter<SimilarMovieAdapter.SimilarMovieViewHolder>() {

    private val similarMovies = ArrayList<Movie>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.similar_movie_item, parent, false)
        return SimilarMovieViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return similarMovies.size
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val movie = similarMovies[position]
        holder.title.text = movie.title
        holder.rating.text = movie.vote_average.toString()
        GlideApp.with(context)
            .load(ConfigInfo.imageUrl + movie.poster_path)
            .skipMemoryCache(false)
            .into(holder.posterImage)
    }

    fun addAll(list: ArrayList<Movie>?) {
        list?.let {
            similarMovies.addAll(it)
            notifyDataSetChanged()
        }
    }


    class SimilarMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var posterImage: ImageView = itemView.findViewById(R.id.poster_image)
        var title: TextView = itemView.findViewById(R.id.tv_title)
        var rating: TextView = itemView.findViewById(R.id.tv_rating)
    }
}