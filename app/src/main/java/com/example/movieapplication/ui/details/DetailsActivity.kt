package com.example.movieapplication.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.network.model.Movie
import com.example.movieapplication.network.model.MovieCast
import com.example.movieapplication.ui.details.adapter.SimilarMovieAdapter
import com.example.movieapplication.util.ConfigInfo.imageUrl
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.cast_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsActivity : BaseActivity<DetailsPresenter>(), IDetailsScreen {

    private var adapter: SimilarMovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initComponents()
        GlobalScope.launch(
            Dispatchers.IO
        ) { presenter?.getMovieDetails(intent.getStringExtra("id")) }
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
        showMainLoading()
    }

    private fun initRecyclerView() {
        adapter = SimilarMovieAdapter(this)
        rv_similar_movies.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_similar_movies.adapter = adapter
    }

    private fun setGenres(movie: Movie) {
        if (movie.genres.size > 0) {
            var genresToShow: String = movie.genres[0].name
            for (i in 1 until movie.genres.size) {
                genresToShow += (", " + movie.genres[i].name)
            }
            details_genres.text = genresToShow
        }
    }

    @SuppressLint("InflateParams")
    override fun showCast(cast_list: ArrayList<MovieCast.Cast>?) {
        cast_list?.let {
            for (cast in it) {
                val itemView = LayoutInflater.from(this).inflate(R.layout.cast_list_item, null)
                itemView.cast_list_name.text = cast.name
                itemView.cast_list_character.text = cast.character
                Glide.with(this)
                    .load(imageUrl + cast.profile_path)
                    .into(itemView.cast_list_image)
            }
        }

    }

    override fun showDetails(movie: Movie?) {
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
        }
    }

    override fun showSimilarMovies(similarMovies: ArrayList<Movie>?) {
        adapter?.addAll(similarMovies)
    }

    private fun showLoading() {
    }

    private fun hideLoading() {
    }
}
