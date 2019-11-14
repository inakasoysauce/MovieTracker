package com.example.movieapplication.ui.friend_favourites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.friend_favourites.adapter.FriendFavouriteAdapter
import com.example.movieapplication.ui.person_details.PersonDetailsActivity
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import kotlinx.android.synthetic.main.fragment_favourites.*

class FriendFavouriteFragment : BaseFragment<FriendFavouritePresenter, FriendFavouriteView>(), FriendFavouriteView, MovieOrSeriesClickedListener {


    private var adapter: FriendFavouriteAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getFavourites(arguments!!.getString("type")!!, arguments!!.getString("uid")!!)
    }

    override fun initComponents() {
        initRecyclerView()
        favourites_title.text = getString(R.string.favourites_title, arguments?.getString("username"))
    }

    private fun initRecyclerView() {
        adapter = FriendFavouriteAdapter(this)
        rv_favourites.adapter = adapter
        rv_favourites.layoutManager = LinearLayoutManager(context)
    }

    override fun showFavourites(list: ArrayList<SearchResultItem>) {
        adapter?.addAll(list)
    }

    override fun goToDetails(id: String, type: String) {
        val intent = if (type != SearchResultItem.person) Intent(activity, DetailsActivity::class.java)
        else Intent(activity, PersonDetailsActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    override fun getViewContext(): Context {
        return context!!
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_right
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_right
    }

    override fun createPresenter(): FriendFavouritePresenter {
        return FriendFavouritePresenter(this)
    }
}