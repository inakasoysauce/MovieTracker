package com.example.movieapplication.ui.cast_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.CastResponse
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.PersonClickedListener
import com.squareup.picasso.Picasso

class CastListAdapter(private val listener: PersonClickedListener) : RecyclerView.Adapter<CastListAdapter.CastListViewHolder>() {

    private val castList = ArrayList<CastResponse.Cast>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_list_item, parent, false)
        return CastListViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: CastListViewHolder, position: Int) {
        val item = castList[position]
        holder.name.text = item.name
        holder.character.text = item.character
        holder.id = item.id.toString()
        Picasso.get()
            .load(ConfigInfo.imageUrl + item.profile_path)
            .into(holder.poster)

    }

    fun addAll(list: ArrayList<CastResponse.Cast>) {
        castList.addAll(list)
        notifyDataSetChanged()
    }

    class CastListViewHolder(itemView: View, listener: PersonClickedListener) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.cast_list_image)
        val name: TextView = itemView.findViewById(R.id.cast_list_name)
        val character: TextView = itemView.findViewById(R.id.cast_list_character)
        var id: String? = null

        init {
            itemView.setOnClickListener {
                if (id != null)
                    listener.goToPersonDetails(id!!)
            }
        }
    }
}