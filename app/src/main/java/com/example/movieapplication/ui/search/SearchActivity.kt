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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.base.SlidableFragment
import com.example.movieapplication.event_bus.Events
import com.example.movieapplication.network.NetworkStatus
import com.example.movieapplication.network.firebase.FirebaseInteractor
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.receiver.NetworkMonitor
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.favourites.FavouriteFragment
import com.example.movieapplication.ui.friend_action.FriendActionFragment
import com.example.movieapplication.ui.friend_favourites.FriendFavouriteFragment
import com.example.movieapplication.ui.friends.FriendFragment
import com.example.movieapplication.ui.login.LoginFragment
import com.example.movieapplication.ui.login.LoginFragmentListener
import com.example.movieapplication.ui.person_details.PersonDetailsActivity
import com.example.movieapplication.ui.profile.ProfileActivity
import com.example.movieapplication.ui.recommendations.RecommendationFragment
import com.example.movieapplication.ui.search.adapter.SearchResultAdapter
import com.example.movieapplication.ui.search.not_logged_in_dialog.NotLoggedInDialogFragment
import com.example.movieapplication.user.User
import com.example.movieapplication.util.Constants.ALL
import com.example.movieapplication.util.Constants.FRIENDS
import com.example.movieapplication.util.Constants.RECEIVED
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding.widget.RxTextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.acivity_search.*
import kotlinx.android.synthetic.main.main_header.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : BaseActivity<SearchPresenter, SearchView>(),
    SearchView,
    SearchResultAdapter.SearchResultClickListener,
    LoginFragmentListener, NotLoggedInDialogFragment.NotLoggedInDialogFragmentListener,
    FriendFragment.FriendFragmentListener, FriendActionFragment.FriendActionFragmentListener {

    private var adapter: SearchResultAdapter? = null

    var shouldCloseFavourites = false

    private var saved: Boolean = false

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private var loginButton: TextView? = null
    private var username: TextView? = null
    private var profilePicture: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_search)
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
        adapter = SearchResultAdapter(this)
        subscribeOnEvents()
        setTextChangeEvent()
        initDoneButton()
        setSearchButton()
        initNavigationView()
        showUserData()
        setLoadingView(loading_screen)
        startArrowAnimation()
    }

    private fun subscribeOnEvents() {
        networkMonitor.enable(this)
        Events.networkEvent += { status ->
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

    override fun onResume() {
        super.onResume()
        setProfilePicture()
        hideFavourites()
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
        }
    }

    private fun setProfilePicture() {
        if (User.picture_path != null) {
            Picasso.get()
                .load(User.picture_path)
                .into(profilePicture)
        } else {
            profilePicture?.setImageDrawable(getDrawable(R.drawable.dummy_profile_picture))
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
                R.id.persons -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    goToFavourites(SearchResultItem.person)
                    return@setNavigationItemSelectedListener true
                }
                R.id.movies -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    goToFavourites(SearchResultItem.movie)
                    return@setNavigationItemSelectedListener true
                }
                R.id.recommendations -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    showRecommendations()
                    return@setNavigationItemSelectedListener true
                }
                R.id.my_friends -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    showFriendFragment(FRIENDS)
                    return@setNavigationItemSelectedListener true
                }
                R.id.friend_requests -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    showFriendFragment(RECEIVED)
                    return@setNavigationItemSelectedListener true
                }
                R.id.get_new_friends -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    showFriendFragment(ALL)
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
                presenter?.getSearchResults(title)
            }
        }
    }

    override fun goToDetails(item: SearchResultItem) {
        if (item.isMovie() || item.isTvShow()) {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("id", item.id.toString())
            intent.putExtra("type", item.mediaType)
            startActivity(intent)
        } else if (item.isPerson()) {
            val intent = Intent(this, PersonDetailsActivity::class.java)
            intent.putExtra("id", item.id.toString())
            startActivity(intent)
        }
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

    override fun addSearchResults(movies: ArrayList<SearchResultItem>?) {
        hideLoading()
        hideArrow()
        movies?.let {
            adapter?.addMovies(it)
        }
    }

    private fun showFragment(fragment: SlidableFragment) {
        showFragment(fragment, R.id.fragment_container)
        hideSearchBar()
        drawer_layout.closeDrawer(GravityCompat.START)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun hideContainedFragment(fragment: SlidableFragment) {
        hideFragment(fragment, ::showSearchBar)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun hideFragment(fragment: SlidableFragment, runOnCommit: () -> Unit) {
        super.hideFragment(fragment, runOnCommit)
        if (visibleFragment == null) {
            showSearchBar()
        }
    }

    private fun hideFavourites() {
        if (visibleFragment is FavouriteFragment && shouldCloseFavourites) {
            hideFragment(visibleFragment!!, ::showSearchBar)
            shouldCloseFavourites = false
        }
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun loggedIn() {
        if (visibleFragment != null)
            hideFragment(visibleFragment!!)
        username?.text = User.username
        loginButton?.text = getString(R.string.logout)
        setProfilePicture()
    }

    override fun getViewContext(): Context {
        return this
    }

    private fun showFriendFragment(type: String) {
        if (User.loggedIn) {
            val bundle = Bundle()
            bundle.putString("type", type)
            showFragment(FriendFragment(), R.id.fragment_container, {}, bundle)
            hideSearchBar()
            drawer_layout.closeDrawer(GravityCompat.START)
            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            showNotLoggedInDialog()
        }
    }

    override fun showUser(uid: String, username: String, picturePath: String?) {
        val bundle = Bundle()
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        bundle.putString("picture_path", picturePath)
        showFragment(FriendActionFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun goToProfile() {
        if (User.loggedIn)
            startActivity(Intent(this, ProfileActivity::class.java))
        else {
            showNotLoggedInDialog()
        }
    }

    private fun goToFavourites(type: String) {
        if (User.loggedIn) {
            val bundle = Bundle()
            bundle.putString("type", type)
            val favouriteFragment = FavouriteFragment()
            favouriteFragment.arguments = bundle
            showFragment(favouriteFragment)
        } else {
            showNotLoggedInDialog()
        }
    }

    private fun showRecommendations() {
        if(User.loggedIn){
            showFragment(RecommendationFragment())
        } else {
            showNotLoggedInDialog()
        }
    }

    override fun showFavouriteMoviesFriend(uid: String, username: String) {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.movie)
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        showFragment(FriendFavouriteFragment(), R.id.fragment_container, {}, bundle)
    }

    override fun showFavouritePersonsFriend(uid: String, username: String) {
        val bundle = Bundle()
        bundle.putString("type", SearchResultItem.person)
        bundle.putString("uid", uid)
        bundle.putString("username", username)
        showFragment(FriendFavouriteFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun showNotLoggedInDialog() {
        showFragment(NotLoggedInDialogFragment(), R.id.fragment_container)
        drawer_layout.closeDrawer(GravityCompat.START)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun goToLoginScreen() {
        hideFragment(visibleFragment!!)
        showFragment(LoginFragment(), R.id.fragment_container)
        hideSearchBar()
    }

    override fun onCancel() {
        hideFragment(visibleFragment!!)
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

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

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Snackbar.make(main_layout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun logOut() {
        FirebaseInteractor.logOut()
        profilePicture?.setImageDrawable(getDrawable(R.drawable.dummy_profile_picture))
        username?.text = getString(R.string.not_logged_in)
        loginButton?.text = getString(R.string.login)
    }

}
