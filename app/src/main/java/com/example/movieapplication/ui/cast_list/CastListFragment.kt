package com.example.movieapplication.ui.cast_list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.network.model.CastResponse
import com.example.movieapplication.ui.cast_list.adapter.CastListAdapter
import com.example.movieapplication.ui.person_details.PersonDetailsActivity
import com.example.movieapplication.util.PersonClickedListener
import kotlinx.android.synthetic.main.fragment_cast_list.*

class CastListFragment : BaseFragment<CastListPresenter, CastListView>(), CastListView, PersonClickedListener {

    private var adapter: CastListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cast_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getCastList(arguments?.getString("id"),arguments?.getString("type"))
        showLoading()
    }

    override fun initComponents() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = CastListAdapter(this)
        rv_cast_list.layoutManager = LinearLayoutManager(context)
        rv_cast_list.adapter = adapter
    }

    override fun showCastList(list: ArrayList<CastResponse.Cast>?) {
        hideLoading()
        if (list != null)
            adapter?.addAll(list)
    }

    override fun goToPersonDetails(id: String) {
        val intent = Intent(activity, PersonDetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        rv_cast_list.visibility = View.GONE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        rv_cast_list.visibility = View.VISIBLE
    }

    override fun getViewContext(): Context {
        return super.getContext()!!
    }

    override fun showError(message: String) {
        super.showError(message)
        hideLoading()
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_right
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_right
    }

    override fun createPresenter(): CastListPresenter {
        return CastListPresenter(this)
    }
}