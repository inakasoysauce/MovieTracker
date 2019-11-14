package com.example.movieapplication.ui.favourites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favourite_item.view.*
import kotlinx.android.synthetic.main.search_result_item.view.poster_image
import kotlinx.android.synthetic.main.search_result_item.view.rating_text_view
import kotlinx.android.synthetic.main.search_result_item.view.release_date_text_view
import kotlinx.android.synthetic.main.search_result_item.view.title_text_view

class FavouriteAdapter(private val listener: FavouriteRemoveListener) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    private val favourites = ArrayList<SearchResultItem>()

    interface FavouriteRemoveListener : MovieOrSeriesClickedListener {
        fun removeFavourite(id: String?, type: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_item, parent, false)
        return FavouriteViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val item = favourites[position]
        holder.title.text = item.title
        holder.releaseDate.text = item.getFirstInfo()
        holder.rating.text = item.getSecondInfo(listener.getViewContext())
        Picasso.get()
            .load(ConfigInfo.imageUrl + item.posterPath)
            .into(holder.poster)
        holder.itemView.setOnClickListener {
            if (item.id != null) {
                listener.goToDetails(item.id.toString(), item.mediaType!!)
            }
        }
        holder.removeButton.setOnClickListener {
            if (item.id != null) {
                listener.removeFavourite(item.id.toString(), item.mediaType!!)
            }
        }
    }

    fun addAll(list: ArrayList<SearchResultItem>) {
        favourites.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(id: String, type: String) {
        val item = favourites.find {
            it.id.toString() == id && it.mediaType == type
        }
        val index = favourites.indexOf(item)
        favourites.remove(item)
        notifyItemRemoved(index)
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.title_text_view
        val releaseDate: TextView = itemView.release_date_text_view
        val rating: TextView = itemView.rating_text_view
        val poster: ImageView = itemView.poster_image
        val removeButton: ImageView = itemView.btn_delete
    }
}