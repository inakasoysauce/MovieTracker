package com.example.movieapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.event_bus.EventBus
import com.example.movieapplication.event_bus.NetworkEvent
import com.example.movieapplication.network.NetworkStatus
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.receiver.NetworkMonitor
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.search.adapter.MovieAdapter
import com.jakewharton.rxbinding.widget.RxTextView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.acivity_main.*
import kotlinx.android.synthetic.main.main_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ISearchScreen, MovieAdapter.MovieClickedListener {

    private var adapter: MovieAdapter? = null

    private var eventBusDisposable: Disposable? = null

    private var saved: Boolean = false

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    lateinit var presenter: SearchPresenter

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_main)
        initComponents()
        initRecyclerView()
    }

    init {
        MovieTracker.movieComponent.inject(this)
    }


    override fun initComponents() {
        adapter = MovieAdapter(this)
        presenter.addView(this)
        subscribeOnNetworkStatusEvent()
        setTextChangeEvent()
        initDoneButton()
        initNavigationView()
    }

    @Suppress("DEPRECATION")
    private fun subscribeOnNetworkStatusEvent() {
        /*val networkStatusReceiver = NetworkStatusReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStatusReceiver,intentFilter)*/
        networkMonitor.enable(this)
        NetworkEvent.event += { status ->
            Log.i("NetworkStatus","Received")
            if (status == NetworkStatus.ONLINE) {
                searchForMovie(search_text.text.toString())
            }
        }
    }

    private fun setTextChangeEvent() {
        RxTextView.textChanges(search_text)
            .filter { charSequence ->
                charSequence.length > 2
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .map { charSequence ->
                charSequence.toString()
            }
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe { string ->
                if (!saved) {
                    searchForMovie(string)
                } else
                    saved = false
            }
    }

    private fun initNavigationView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val toggle = ActionBarDrawerToggle(this,drawer_layout,R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        btn_menu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    private fun initDoneButton() {
        search_text.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchForMovie(search_text.text.toString())
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initRecyclerView() {
        search_recycler_view.layoutManager = LinearLayoutManager(this)
        search_recycler_view.adapter = adapter
    }

    private fun searchForMovie(title: String) {
        if (title.isNotEmpty()) {
            showLoading()
            error_text_view.visibility = TextView.GONE
            adapter?.clear()
            GlobalScope.launch(Dispatchers.Main) {
                presenter.getMovies(title)
            }
        }
    }

    override fun goToDetails(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun noResult() {
        hideLoading()
        setErrorTextView(getString(R.string.no_results))
    }

    override fun showNoConnectionMessage() {
        hideLoading()
        setErrorTextView(getString(R.string.no_connection))
    }

    private fun setErrorTextView(message: String) {
        error_text_view.text = message
        error_text_view.visibility = TextView.VISIBLE
    }

    override fun addMovies(movies: ArrayList<SearchResultItem>?) {
        hideLoading()
        movies?.let {
            adapter?.addMovies(it)
        }
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun getContext(): Context {
        return this
    }

    override fun getCharSequence(viewID: Int, voteAverage: String, voteCount: String): CharSequence? {
        return getString(viewID, voteAverage, voteCount)
    }

    override fun onDestroy() {
        presenter.destroyView()
        eventBusDisposable?.dispose()
        super.onDestroy()
    }

    private fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("movies", adapter?.getMovies())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter?.addMovies(savedInstanceState.getParcelableArrayList("movies"))
        saved = true
    }
}
