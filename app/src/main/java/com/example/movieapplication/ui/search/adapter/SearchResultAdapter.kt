package com.example.movieapplication.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseAdapterListener
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.util.ConfigInfo.imageUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_result_item.view.*

class SearchResultAdapter(private val listener: SearchResultClickListener) : RecyclerView.Adapter<SearchResultAdapter.MovieViewHolder>() {

    private var searchResults: ArrayList<SearchResultItem> = ArrayList()

    interface SearchResultClickListener : BaseAdapterListener {
        fun goToDetails(item: SearchResultItem)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val itemView = LayoutInflater
            .from(p0.context)
            .inflate(R.layout.search_result_item, p0, false)
        return MovieViewHolder(itemView, listener)

    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val searchResultItem = searchResults[position]
        holder.titleTv.text = searchResultItem.title
        holder.releaseDateTv.text = searchResultItem.getFirstInfo()
        holder.ratingTv.text = searchResultItem.getSecondInfo(listener.getViewContext())
        holder.item = searchResultItem
        Picasso.get()
            .load(imageUrl + searchResultItem.posterPath)
            .into(holder.posterIv)
    }

    fun addMovies(movies: ArrayList<SearchResultItem>?) {
        movies?.let {
            this.searchResults.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun clear() {
        searchResults.clear()
        notifyDataSetChanged()
    }

    class MovieViewHolder(itemView: View, listener: SearchResultClickListener) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.title_text_view
        val releaseDateTv: TextView = itemView.release_date_text_view
        val ratingTv: TextView = itemView.rating_text_view
        val posterIv: ImageView = itemView.poster_image
        var item : SearchResultItem? = null

        init {
            itemView.setOnClickListener {
                if (item != null)
                    listener.goToDetails(item!!)
            }
        }
    }
}