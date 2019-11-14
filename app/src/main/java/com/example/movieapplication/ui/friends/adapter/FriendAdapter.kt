package com.example.movieapplication.ui.friends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapplication.R
import com.example.movieapplication.network.model.FriendItem
import com.example.movieapplication.user.User
import com.example.movieapplication.util.Constants
import com.example.movieapplication.util.UserClickedListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendAdapter(private val listener: UserClickedListener, private val type: String) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private val friends = ArrayList<FriendItem>()

    private var buttonWidth: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        if (buttonWidth == null) {
            buttonWidth = itemView.btn_send_request.layoutParams.width
        }
        return FriendViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = friends[position]
        holder.name.text = user.username
        Picasso.get()
            .load(user.picture_path)
            .placeholder(R.drawable.dummy_profile_picture)
            .into(holder.profileImg)
        when {
            User.alreadyFriend(user.uid!!) -> setAlreadyFriend(holder)
            User.alreadySentRequest(user.uid!!) -> setRequestAlreadySent(holder)
            User.alreadyRequestReceived(user.uid!!) -> setRequestAlreadyReceived(holder)
            else -> setToDefault(holder)
        }
        holder.itemView.setOnClickListener {
            listener.userClicked(user)
        }
        holder.sendRequestButton.setOnClickListener {
            listener.sendRequest(user.uid!!)
        }
        holder.acceptButton.setOnClickListener {
            listener.acceptRequest(user.uid!!)
        }
        holder.deleteButton.setOnClickListener {
            listener.deleteRequest(user.uid!!)
        }
    }

    private fun setAlreadyFriend(holder: FriendViewHolder) {
        holder.sendRequestButton.visibility = View.GONE
        holder.icon.visibility = View.GONE
    }

    private fun setRequestAlreadySent(holder: FriendViewHolder) {
        val params = holder.sendRequestButton.layoutParams
        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val button = holder.sendRequestButton
        button.layoutParams = params
        holder.icon.visibility = View.GONE
        button.isEnabled = false
        button.text = listener.getViewContext().getString(R.string.request_sent)
        button.background = listener.getViewContext().getDrawable(R.drawable.favourite_already)
    }

    private fun setRequestAlreadyReceived(holder: FriendViewHolder) {
        holder.sendRequestButton.visibility = View.GONE
        holder.icon.visibility = View.GONE
        holder.acceptButton.visibility = View.VISIBLE
        holder.deleteButton.visibility = View.VISIBLE
    }

    private fun setToDefault(holder: FriendViewHolder) {
        val params = holder.sendRequestButton.layoutParams
        params.width = buttonWidth!!
        holder.sendRequestButton.visibility = View.VISIBLE
        holder.sendRequestButton.layoutParams = params
        holder.icon.visibility = View.VISIBLE
        holder.sendRequestButton.background = listener.getViewContext()
            .getDrawable(R.drawable.rounded_button)
        holder.sendRequestButton.isEnabled = true
        holder.sendRequestButton.text = null
        holder.deleteButton.visibility = View.GONE
        holder.acceptButton.visibility = View.GONE
    }

    fun addAll(list: ArrayList<FriendItem>?) {
        list?.let {
            friends.clear()
            friends.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun update(user: String) {
        val item = friends.find {
            it.uid == user
        }
        val index = friends.indexOf(item)
        when (type) {
            Constants.FRIENDS -> {
                if (!User.alreadyFriend(user) && item != null) {
                    friends.remove(item)
                    notifyItemRemoved(index)
                } else if (item == null) {
                    listener.reload()
                }
            }
            Constants.RECEIVED -> {
                if (!User.alreadySentRequest(user) && item != null) {
                    friends.remove(item)
                    notifyItemRemoved(index)
                } else if (item == null) {
                    listener.reload()
                }
            }
            else -> notifyItemChanged(index)
        }
    }

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImg: ImageView = itemView.picture_friend
        var name: TextView = itemView.tv_friend_name
        var sendRequestButton: Button = itemView.btn_send_request
        var acceptButton: Button = itemView.btn_accept
        var deleteButton: Button = itemView.btn_delete
        var icon: ImageView = itemView.add_friend_icon
    }
}