package com.example.movieapplication.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.network.model.MovieOrSeries
import com.example.movieapplication.ui.cast_list.CastListFragment
import com.example.movieapplication.ui.choose_friend.ChooseFriendFragment
import com.example.movieapplication.ui.details.adapter.SimilarMovieOrSeriesAdapter
import com.example.movieapplication.ui.details.rating.RatingFragment
import com.example.movieapplication.ui.pictures.PicturesFragment
import com.example.movieapplication.user.User
import com.example.movieapplication.util.ConfigInfo.imageUrl
import com.example.movieapplication.util.FriendChooseListener
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsActivity : BaseActivity<DetailsPresenter, DetailsView>(), DetailsView, MovieOrSeriesClickedListener, RatingFragment.RatingFragmentListener, FriendChooseListener {

    private var adapter: SimilarMovieOrSeriesAdapter? = null

    private var id: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initComponents()
        GlobalScope.launch(
            Dispatchers.IO
        ) { presenter?.getDetails(id, type) }
    }

    init {
        MovieTracker.movieComponent.inject(this)
    }

    override fun createPresenter(): DetailsPresenter {
        return DetailsPresenter(this)
    }

    override fun initComponents() {
        setLoadingView(loading_screen)
        initRecyclerView()
        initButtons()
        showMainLoading()
        id = intent.getStringExtra("id")
        type = intent.getStringExtra("type")
    }

    private fun initRecyclerView() {
        adapter = SimilarMovieOrSeriesAdapter(this)
        rv_similar_movies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_similar_movies.adapter = adapter
    }

    private fun initButtons() {
        btn_cast_list.setOnClickListener {
            showCastListFragment()
        }
        btn_go_back.setOnClickListener {
            goBackToSearch()
        }
        btn_pictures.setOnClickListener {
            showPicturesFragment()
        }
        btn_add_to_favourites.setOnClickListener {
            presenter?.addToFavourites(id, type)
        }
        btn_give_rating.setOnClickListener {
            showRatingFragment()
        }
        btn_delete_rating.setOnClickListener {
            presenter?.deleteRating(id, type)
        }
        btn_recommend.setOnClickListener {
            showChooseFriendFragment()
        }
    }

    private fun showCastListFragment() {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("type", type)
        showFragment(CastListFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun showPicturesFragment() {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("type", type)
        showFragment(PicturesFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun showRatingFragment() {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("type", type)
        showFragment(RatingFragment(), R.id.fragment_container, {}, bundle)
    }

    private fun showChooseFriendFragment() {
        showFragment(ChooseFriendFragment(), R.id.fragment_container)
    }

    override fun friendChoosed(friendID: String) {
        hideVisibleFragment()
        presenter?.recommend(friendID, id, type)
    }

    override fun onRatingGiven(rating: Int) {
        if (visibleFragment != null) {
            hideFragment(visibleFragment!!)
        }
        setRating(rating)
    }

    override fun rateDeleted() {
        btn_delete_rating.visibility = View.GONE
        btn_give_rating.isEnabled = true
        btn_give_rating.text = getString(R.string.give_rating)
    }

    override fun onCancel() {
        if (visibleFragment != null)
            hideFragment(visibleFragment!!)
    }

    private fun setGenres(movie: MovieOrSeries) {
        if (movie.genres!!.size > 0) {
            var genresToShow: String? = movie.genres[0].name
            for (i in 1 until movie.genres.size) {
                genresToShow += (", " + movie.genres[i].name)
            }
            details_genres.text = genresToShow
        }
    }

    override fun showDetails(movie: MovieOrSeries?) {
        hideMainLoading()
        movie?.let {
            details_title.text = it.title
            details_release_date.text = it.release_date
            details_description.text = it.overview
            details_rating.text = it.vote_average.toString()
            details_vote_count.text = getString(R.string.details_vote_count, it.vote_count.toString())
            setGenres(it)
            Glide.with(this)
                .load(imageUrl + it.poster_path)
                .into(details_poster)
            Picasso.get()
                .load(imageUrl + it.poster_path)
                .into(details_poster)
            checkIfFavourite(movie)
            checkRating(movie)
        }
    }

    override fun addedToFavourites() {
        setFavourite()
    }

    private fun setFavourite() {
        gif_image.playAnimation()
        btn_add_to_favourites.background = getDrawable(R.drawable.favourite_already)
        btn_add_to_favourites.text = getString(R.string.favourite_already)
        btn_add_to_favourites.isEnabled = false
    }

    private fun setRating(rating: Int) {
        btn_give_rating.text = getString(R.string.your_rating, rating)
        btn_give_rating.isEnabled = false
        btn_delete_rating.visibility = View.VISIBLE
    }

    private fun checkIfFavourite(movie: MovieOrSeries) {
        if (User.isFavourite(movie)) {
            setFavourite()
        }
    }

    private fun checkRating(movie: MovieOrSeries) {
        val rating = User.getRate(movie)
        if (rating > -1) {
            setRating(rating)
        }
    }

    override fun showSimilarMoviesOrSeries(moviesOrSeries: ArrayList<MovieOrSeries>?) {
        adapter?.addAll(moviesOrSeries)
    }

    override fun goToDetails(id: String, type: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    override fun getViewContext(): Context {
        return this
    }

    override fun showError(message: String) {
        hideMainLoading()
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show()
    }
}
