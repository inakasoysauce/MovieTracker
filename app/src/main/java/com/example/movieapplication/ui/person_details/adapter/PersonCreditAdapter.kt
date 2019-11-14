package com.example.movieapplication.ui.person_details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.PersonCredit
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.squareup.picasso.Picasso

class PersonCreditAdapter(private val listener: MovieOrSeriesClickedListener) : RecyclerView.Adapter<PersonCreditAdapter.CreditViewHolder>() {

    private val credits = ArrayList<PersonCredit.Credit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.credit_item, parent, false)
        return CreditViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return credits.size
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        val credit = credits[position]
        holder.title.text = credit.title
        holder.character.text = credit.character
        holder.id = credit.id.toString()
        holder.type = credit.media_type
        Picasso.get()
            .load(ConfigInfo.imageUrl + credit.poster_path)
            .into(holder.posterImage)
    }

    fun addAll(list: ArrayList<PersonCredit.Credit>?) {
        list?.let {
            credits.addAll(it)
            notifyDataSetChanged()
        }
    }


    class CreditViewHolder(itemView: View, listener: MovieOrSeriesClickedListener) : RecyclerView.ViewHolder(itemView) {

        var posterImage: ImageView = itemView.findViewById(R.id.poster_image)
        var title: TextView = itemView.findViewById(R.id.tv_title)
        var character: TextView = itemView.findViewById(R.id.tv_character)
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