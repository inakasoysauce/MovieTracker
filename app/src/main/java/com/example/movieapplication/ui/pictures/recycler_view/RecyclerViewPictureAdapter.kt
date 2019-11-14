package com.example.movieapplication.ui.pictures.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.util.PictureClickedListener
import com.example.movieapplication.util.PictureTransforms
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class RecyclerViewPictureAdapter(private val listener: PictureClickedListener) : RecyclerView.Adapter<RecyclerViewPictureAdapter.PictureViewHolder>() {

    private var urlList = ArrayList<String>()

    private var selectedPicture = 0
    private var picasso: Picasso? = null

    private val cacheMap = hashMapOf<Int, ImageView?>()

    init {
        picasso = Picasso.Builder(listener.getViewContext()).build()
        picasso?.isLoggingEnabled = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_picture_item, parent, false)
        return PictureViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val url = urlList[position]

        if (cacheMap[position] != holder.picture) {
            picasso?.load(url)?.into(holder.picture, object : Callback {
                override fun onSuccess() {
                    cacheMap[position] = holder.picture
                }

                override fun onError(e: Exception?) {}

            })
        }

        if (selectedPicture != holder.adapterPosition) {
            PictureTransforms.setColorless(holder.picture)
        } else {
            PictureTransforms.setColored(holder.picture)
        }

        holder.itemView.setOnClickListener {
            listener.showPicture(holder.adapterPosition)
        }
    }

    fun setSelectedPicture(position: Int) {
        selectedPicture = position
        notifyDataSetChanged()
    }

    fun addAll(list: ArrayList<String>?) {
        list?.let {
            urlList.addAll(it)
            notifyDataSetChanged()
        }
    }

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var picture: ImageView = itemView.findViewById(R.id.rv_picture)
    }
}