package com.example.movieapplication.ui.friend_action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_friend_action.*

class FriendActionFragment : BaseFragment<FriendActionPresenter, FriendActionView>(), FriendActionView {

    private val delete = "DELETE"
    private val cancel = "CANCEL"

    private var listener: FriendActionFragmentListener? = null

    interface FriendActionFragmentListener {
        fun showFavouriteMoviesFriend(uid: String, username: String)
        fun showFavouritePersonsFriend(uid: String, username: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_action, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FriendActionFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.checkStatus(arguments)
        initComponents()
    }

    override fun initComponents() {
        initButtons()
    }

    private fun initButtons() {
        btn_send_request.setOnClickListener {
            presenter?.sendRequest()
        }
        btn_delete_friend.setOnClickListener {
            if (btn_delete_friend.tag == delete) {
                presenter?.deleteFriend()
            } else {
                presenter?.cancelRequest()
            }
        }

        btn_accept.setOnClickListener {
            presenter?.acceptRequest()
        }
        btn_delete_request.setOnClickListener {
            presenter?.deleteRequest()
        }
        btn_show_favourite_actors.setOnClickListener {
            listener?.showFavouritePersonsFriend(presenter!!.uid!!, presenter!!.username!!)
        }
        btn_show_favourite_movies.setOnClickListener {
            listener?.showFavouriteMoviesFriend(presenter!!.uid!!, presenter!!.username!!)
        }
    }

    override fun showAsFriend(username: String, picturePath: String?) {
        btn_send_request.visibility = View.GONE
        btn_accept.visibility = View.GONE
        btn_delete_friend.visibility = View.VISIBLE
        btn_show_favourite_actors.visibility = View.VISIBLE
        btn_show_favourite_movies.visibility = View.VISIBLE
        btn_delete_request.visibility = View.GONE
        btn_delete_friend.text = getString(R.string.delete_friend)
        btn_delete_friend.tag = delete
        btn_show_favourite_movies.text = getString(R.string.see_friend_s_favourite_movies_and_series, username)
        btn_show_favourite_actors.text = getString(R.string.see_friend_s_favourite_actors_and_directors, username)
        setUsername(username)
        loadProfilePicture(picturePath)
    }

    override fun showAsRequested(username: String, picturePath: String?) {
        btn_send_request.visibility = View.GONE
        btn_accept.visibility = View.VISIBLE
        btn_delete_friend.visibility = View.GONE
        btn_show_favourite_movies.visibility = View.GONE
        btn_show_favourite_actors.visibility = View.GONE
        btn_delete_request.visibility = View.VISIBLE
        setUsername(username)
        loadProfilePicture(picturePath)
    }

    override fun showAsRequestSent(username: String, picturePath: String?) {
        btn_send_request.visibility = View.GONE
        btn_accept.visibility = View.GONE
        btn_delete_friend.visibility = View.VISIBLE
        btn_show_favourite_actors.visibility = View.GONE
        btn_show_favourite_movies.visibility = View.GONE
        btn_delete_request.visibility = View.GONE
        btn_delete_friend.text = getString(R.string.cancel_request)
        btn_delete_friend.tag = cancel
        setUsername(username)
        loadProfilePicture(picturePath)
    }

    override fun showAsDefault(username: String, picturePath: String?) {
        btn_send_request.visibility = View.VISIBLE
        btn_accept.visibility = View.GONE
        btn_delete_friend.visibility = View.GONE
        btn_show_favourite_actors.visibility = View.GONE
        btn_show_favourite_movies.visibility = View.GONE
        btn_delete_request.visibility = View.GONE
        setUsername(username)
        loadProfilePicture(picturePath)
    }

    private fun loadProfilePicture(picturePath: String?) {
        Picasso.get()
            .load(picturePath)
            .placeholder(R.drawable.dummy_profile_picture)
            .into(profile_img)
    }

    private fun setUsername(username: String) {
        tv_username.text = username
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_bottom
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_bottom
    }

    override fun createPresenter(): FriendActionPresenter {
        return FriendActionPresenter(this)
    }
}