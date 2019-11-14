package com.example.movieapplication.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.MovieOrSeries
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.squareup.picasso.Picasso

class SimilarMovieOrSeriesAdapter(private val listener: MovieOrSeriesClickedListener) : RecyclerView.Adapter<SimilarMovieOrSeriesAdapter.SimilarMovieViewHolder>() {

    private val similarMovies = ArrayList<MovieOrSeries>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.similar_movie_item, parent, false)
        return SimilarMovieViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return similarMovies.size
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val movieOrSeries = similarMovies[position]
        holder.title.text = movieOrSeries.title
        holder.rating.text = movieOrSeries.vote_average.toString()
        holder.id = movieOrSeries.id.toString()
        holder.type = movieOrSeries.type
        Picasso.get()
            .load(ConfigInfo.imageUrl + movieOrSeries.poster_path)
            .into(holder.posterImage)
    }

    fun addAll(list: ArrayList<MovieOrSeries>?) {
        list?.let {
            similarMovies.addAll(it)
            notifyDataSetChanged()
        }
    }


    class SimilarMovieViewHolder(itemView: View, listener: MovieOrSeriesClickedListener) : RecyclerView.ViewHolder(itemView) {

        var posterImage: ImageView = itemView.findViewById(R.id.poster_image)
        var title: TextView = itemView.findViewById(R.id.tv_title)
        var rating: TextView = itemView.findViewById(R.id.tv_rating)
        var id: String? = null
        var type: String? = null
        init {
            itemView.setOnClickListener {
                if (id != null && type != null)
                listener.goToDetails(id!!, type!!)
            }
        }
    }
}