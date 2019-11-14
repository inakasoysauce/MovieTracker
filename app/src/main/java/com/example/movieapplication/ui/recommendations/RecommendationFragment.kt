package com.example.movieapplication.ui.recommendations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.event_bus.Events
import com.example.movieapplication.network.model.RecommendationItem
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.recommendations.adapter.RecommendationAdapter
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.example.movieapplication.util.NotificationEvent
import kotlinx.android.synthetic.main.fragment_recommendations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecommendationFragment : BaseFragment<RecommendationPresenter, RecommendationView>(), RecommendationView, RecommendationAdapter.RecommendationClickedListener {


    private var adapter: RecommendationAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
        presenter?.getRecommendations()
    }

    override fun initComponents() {
        initRecyclerView()
        subscribeOnEvent()
    }

    private fun initRecyclerView() {
        adapter = RecommendationAdapter(this)
        rv_recommendations.adapter = adapter
        rv_recommendations.layoutManager = LinearLayoutManager(context)
    }

    override fun showRecommendations(list: ArrayList<RecommendationItem>?) {
        adapter?.addAll(list)
    }

    override fun removeRecommendation(friendID: String, id: String, type: String) {
        presenter?.deleteRecommendation(friendID, id, type)
    }

    override fun removed(friendID: String, id: String, type: String) {
        adapter?.remove(friendID, id, type)
    }

    override fun goToDetails(id: String, type: String) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    private fun subscribeOnEvent() {
        Events.friendEvent += eventHandler
    }

    private fun unSubscribeFromEvent() {
        Events.friendEvent -= eventHandler
    }

    private val eventHandler: (NotificationEvent) -> Unit = {
        GlobalScope.launch(Dispatchers.Main) {
            if (it.type == "RECOMMENDATION") {
                presenter?.getRecommendations()
            }
        }
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

    override fun createPresenter(): RecommendationPresenter {
        return RecommendationPresenter(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribeFromEvent()
    }
}