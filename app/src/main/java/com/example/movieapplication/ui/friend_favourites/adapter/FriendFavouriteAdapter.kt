package com.example.movieapplication.ui.friend_favourites.adapter

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
import kotlinx.android.synthetic.main.search_result_item.view.*

class FriendFavouriteAdapter(private val listener: MovieOrSeriesClickedListener) : RecyclerView.Adapter<FriendFavouriteAdapter.FriendFavouriteViewHolder>() {

    private val favourites = ArrayList<SearchResultItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendFavouriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_favourite_item, parent, false)
        return FriendFavouriteViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    override fun onBindViewHolder(holder: FriendFavouriteViewHolder, position: Int) {
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
    }

    fun addAll(list: ArrayList<SearchResultItem>) {
        favourites.addAll(list)
        notifyDataSetChanged()
    }

    class FriendFavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.title_text_view
        val releaseDate: TextView = itemView.release_date_text_view
        val rating: TextView = itemView.rating_text_view
        val poster: ImageView = itemView.poster_image
    }
}