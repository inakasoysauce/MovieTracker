package com.example.movieapplication.ui.choose_friend

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.network.model.FriendItem
import com.example.movieapplication.ui.choose_friend.adapter.ChooseFriendAdapter
import com.example.movieapplication.util.FriendChooseListener
import kotlinx.android.synthetic.main.fragment_choose_friend.*

class ChooseFriendFragment : BaseFragment<ChooseFriendPresenter, ChooseFriendView>(), ChooseFriendView, FriendChooseListener {

    private var adapter: ChooseFriendAdapter? = null

    private var listener: FriendChooseListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_friend, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as FriendChooseListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getFriends()
    }

    override fun initComponents() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ChooseFriendAdapter(this)
        rv_choose_friend.adapter = adapter
        rv_choose_friend.layoutManager = LinearLayoutManager(context)
    }

    override fun showFriends(list: ArrayList<FriendItem>?) {
        adapter?.addAll(list)
    }

    override fun friendChoosed(friendID: String) {
        listener?.friendChoosed(friendID)
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_bottom
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_bottom
    }

    override fun createPresenter(): ChooseFriendPresenter {
        return ChooseFriendPresenter(this)
    }
}