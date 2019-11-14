package com.example.movieapplication.ui.recommendations.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.RecommendationItem
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recommendation_item.view.*

class RecommendationAdapter(private val listener: RecommendationClickedListener) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val recommendations = ArrayList<RecommendationItem>()

    interface RecommendationClickedListener : MovieOrSeriesClickedListener {
        fun removeRecommendation(friendID: String, id: String, type: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommendation_item, parent, false)
        return RecommendationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return recommendations.size
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = recommendations[position]
        holder.title.text = item.item?.title
        holder.username.text = item.username
        Picasso.get()
            .load(ConfigInfo.imageUrl + item.item?.poster_path)
            .into(holder.poster)
        holder.itemView.setOnClickListener {
            listener.goToDetails(item.id!!, item.type!!)
        }
        holder.deleteButton.setOnClickListener {
            listener.removeRecommendation(item.uid!!, item.id!!, item.type!!)
        }

    }

    fun addAll(list: ArrayList<RecommendationItem>?) {
        list?.let {
            recommendations.clear()
            recommendations.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun remove(friendID: String, id: String, type: String) {
        val item = recommendations.find {
            it.id == id && it.type == type && it.uid == friendID
        }
        val index = recommendations.indexOf(item)
        recommendations.remove(item)
        notifyItemRemoved(index)
    }

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title_text_view
        val username: TextView = itemView.tv_username
        val poster: ImageView = itemView.poster_image
        val deleteButton: ImageView = itemView.btn_delete
    }
}