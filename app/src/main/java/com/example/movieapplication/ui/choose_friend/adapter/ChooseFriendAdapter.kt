package com.example.movieapplication.ui.choose_friend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.FriendItem
import com.example.movieapplication.util.FriendChooseListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.choose_friend_item.view.*

class ChooseFriendAdapter(private val listener: FriendChooseListener) : RecyclerView.Adapter<ChooseFriendAdapter.ChooseFriendViewHolder>() {

    private val friends = ArrayList<FriendItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseFriendViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.choose_friend_item, parent, false)
        return ChooseFriendViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: ChooseFriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.username.text = friend.username
        Picasso.get()
            .load(friend.picture_path)
            .placeholder(R.drawable.dummy_profile_picture)
            .into(holder.picture)
        holder.itemView.setOnClickListener {
            listener.friendChoosed(friend.uid!!)
        }
    }

    fun addAll(list: ArrayList<FriendItem>?) {
        list?.let {
            friends.clear()
            friends.addAll(it)
            notifyDataSetChanged()
        }
    }

    class ChooseFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val picture: ImageView = itemView.picture_friend
        val username: TextView = itemView.tv_friend_name
    }
}