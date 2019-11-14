package com.example.movieapplication.ui.friends

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.event_bus.Events
import com.example.movieapplication.network.model.FriendItem
import com.example.movieapplication.ui.friends.adapter.FriendAdapter
import com.example.movieapplication.util.Constants
import com.example.movieapplication.util.NotificationEvent
import com.example.movieapplication.util.UserClickedListener
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendFragment : BaseFragment<FriendPresenter, FriendView>(), FriendView, UserClickedListener {


    private var adapter: FriendAdapter? = null
    private var type :String? = null

    private var friendListener: FriendFragmentListener? = null

    interface FriendFragmentListener {
        fun showUser(uid: String, username: String, picturePath: String?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        friendListener = context as FriendFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getUsers(type!!)
    }

    override fun initComponents() {
        type = arguments?.getString("type")!!
        when (type) {
            Constants.ALL -> tv_friends_title.text = getString(R.string.all_users)
            Constants.FRIENDS -> tv_friends_title.text = getString(R.string.my_friends_title)
            else -> tv_friends_title.text = getString(R.string.friend_requests_title)
        }
        initRecyclerView()
        subscribeOnEvents()
    }

    private fun initRecyclerView() {
        adapter = FriendAdapter(this, type!!)
        rv_friends.layoutManager = LinearLayoutManager(context)
        rv_friends.adapter = adapter
    }

    private fun subscribeOnEvents() {
        Events.friendEvent += onFriendNotification
    }

    private fun unsubscribeOnEvents() {
        Events.friendEvent -= onFriendNotification
    }

    private val onFriendNotification: (NotificationEvent) -> Unit = {
        GlobalScope.launch(Dispatchers.Main) {
            when {
                it.type == "REQUEST" -> presenter?.onNewRequest(it.friend)
                it.type == "ACCEPT" -> presenter?.onFriendAcceptedRequest(it.friend)
                it.type == "ACTION" -> adapter?.update(it.friend)
            }
        }
    }

    override fun userClicked(user: FriendItem) {
        friendListener?.showUser(user.uid!!,user.username!!, user.picture_path)
    }

    override fun sendRequest(user: String) {
        presenter?.sendRequest(user)
    }

    override fun acceptRequest(user: String) {
        presenter?.acceptRequest(user)
    }

    override fun deleteRequest(user: String) {
        presenter?.deleteRequest(user)
    }

    override fun showPeople(people: ArrayList<FriendItem>?) {
        adapter?.addAll(people)
    }

    override fun update(user: String) {
        adapter?.update(user)
    }

    override fun reload() {
        presenter?.getUsers(type!!)
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun createPresenter(): FriendPresenter {
        return FriendPresenter(this)
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_right
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_right
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeOnEvents()
    }
}