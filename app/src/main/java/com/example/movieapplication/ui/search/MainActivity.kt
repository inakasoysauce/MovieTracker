package com.example.movieapplication.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.event_bus.NetworkEvent
import com.example.movieapplication.network.NetworkStatus
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.receiver.NetworkMonitor
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.login.LoginFragment
import com.example.movieapplication.ui.login.LoginFragmentListener
import com.example.movieapplication.ui.profile.ProfileActivity
import com.example.movieapplication.ui.search.adapter.MovieAdapter
import com.example.movieapplication.user.User
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.acivity_main.*
import kotlinx.android.synthetic.main.main_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : BaseActivity<SearchPresenter>(), ISearchScreen, MovieAdapter.MovieClickedListener, LoginFragmentListener {

    private var adapter: MovieAdapter? = null

    private var saved: Boolean = false

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private var visibleFragment: Fragment? = null

    private var loginButton: TextView? = null
    private var username: TextView? = null
    private var profilePicture : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_main)
        initComponents()
        initRecyclerView()
    }

    init {
        MovieTracker.movieComponent.inject(this)
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter(this)
    }


    override fun initComponents() {
        adapter = MovieAdapter(this)
        subscribeOnNetworkStatusEvent()
        setTextChangeEvent()
        initDoneButton()
        setSearchButton()
        initNavigationView()
        showUserData()
        setLoadingView(loading_screen)
        startArrowAnimation()
    }

    private fun subscribeOnNetworkStatusEvent() {
        networkMonitor.enable(this)
        NetworkEvent.event += { status ->
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
        val toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        btn_menu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
            search_text.clearFocus()
        }
        val header = navigation_view.getHeaderView(0)
        loginButton = header.findViewById(R.id.tv_logout_login)
        username = header.findViewById(R.id.tv_username)
        profilePicture = header.findViewById(R.id.img_nav_header)
        loginButton?.setOnClickListener {
            if (!User.loggedIn) {
                showFragment(LoginFragment())
            } else
                logOut()
        }
        setMenuListener()
    }

    private fun initDoneButton() {
        search_text.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchForMovie(search_text.text.toString())
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setSearchButton() {
        btn_search.setOnClickListener {
            searchForMovie(search_text.text.toString())
        }
    }

    private fun initRecyclerView() {
        search_recycler_view.layoutManager = LinearLayoutManager(this)
        search_recycler_view.adapter = adapter
    }

    private fun showUserData() {
        if (!User.loggedIn) {
            username?.text = getString(R.string.not_logged_in)
            loginButton?.text = getString(R.string.login)
        } else {
            username?.text = User.username
            loginButton?.text = getString(R.string.logout)
            setProfilePicture()
        }
    }

    private fun setProfilePicture() {
        if (User.profilePicture != null){
            profilePicture?.setImageBitmap(User.profilePicture)
        }
    }

    private fun setMenuListener() {
        navigation_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    goToProfile()
                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }
    }

    private fun searchForMovie(title: String) {
        if (title.isNotEmpty()) {
            showLoading()
            error_text_view.visibility = TextView.GONE
            adapter?.clear()
            GlobalScope.launch(Dispatchers.Main) {
                presenter?.getMovies(title)
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
        hideArrow()
        movies?.let {
            adapter?.addMovies(it)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, android.R.anim.slide_out_right)
            .replace(R.id.fragment_container, fragment)
            .commit()
        hideSearchBar()
        drawer_layout.closeDrawer(GravityCompat.START)
        visibleFragment = fragment
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
            .remove(fragment)
            .runOnCommit {
                showSearchBar()
            }
            .commit()
        visibleFragment = null
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun loggedIn() {
        if (visibleFragment != null)
            hideFragment(visibleFragment!!)
        username?.text = User.username
        loginButton?.text = getString(R.string.logout)
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    override fun getContext(): Context {
        return this
    }

    private fun goToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun getCharSequence(
        viewID: Int,
        voteAverage: String,
        voteCount: String
    ): CharSequence? {
        return getString(viewID, voteAverage, voteCount)
    }

    private fun showSearchBar() {
        search_bar.setExpanded(true, true)
        search_bar.setLiftable(false)
    }

    private fun hideSearchBar() {
        search_bar.setExpanded(false, true)
        search_bar.setLiftable(true)
    }

    private fun startArrowAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
        img_arrow_up.startAnimation(animation)
    }

    private fun hideArrow() {
        arrow_holder.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (visibleFragment == null) {
            super.onBackPressed()
        } else {
            hideFragment(visibleFragment!!)
        }
    }

    private fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    private fun logOut() {
        FirebaseInteractor.logOut()
        username?.text = getString(R.string.not_logged_in)
        loginButton?.text = getString(R.string.login)
    }

}
