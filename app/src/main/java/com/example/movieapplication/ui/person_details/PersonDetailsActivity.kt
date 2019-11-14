package com.example.movieapplication.ui.person_details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseActivity
import com.example.movieapplication.network.model.Person
import com.example.movieapplication.network.model.PersonCredit
import com.example.movieapplication.network.model.SearchResultItem
import com.example.movieapplication.ui.details.DetailsActivity
import com.example.movieapplication.ui.person_details.adapter.PersonCreditAdapter
import com.example.movieapplication.ui.pictures.PicturesFragment
import com.example.movieapplication.user.User
import com.example.movieapplication.util.ConfigInfo
import com.example.movieapplication.util.MovieOrSeriesClickedListener
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details_person.*

class PersonDetailsActivity : BaseActivity<PersonDetailsPresenter, PersonDetailsView>(), PersonDetailsView, MovieOrSeriesClickedListener {

    private var adapter: PersonCreditAdapter? = null

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_person)
        initComponents()
        setLoadingView(loading_screen)
        showMainLoading()
        presenter?.getPersonDetails(id)
    }

    override fun initComponents() {
        initRecyclerView()
        initButtons()
        setBiographyScrollable()
        id = intent.getStringExtra("id")
    }

    private fun initRecyclerView() {
        adapter = PersonCreditAdapter(this)
        rv_credit_list.adapter = adapter
        rv_credit_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initButtons() {
        btn_go_back.setOnClickListener {
            goBackToSearch()
        }
        btn_pictures.setOnClickListener {
            showPicturesFragment()
        }

        btn_add_to_favourites.setOnClickListener {
            presenter?.addToFavourites(id)
        }
    }

    private fun setBiographyScrollable() {
        person_biography.movementMethod = ScrollingMovementMethod()
        scrollview.setOnTouchListener { _, _ ->
            person_biography.parent.requestDisallowInterceptTouchEvent(false)
            false
        }
        person_biography.setOnTouchListener { _, _ ->
            person_biography.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    override fun addedToFavourites() {
        setFavourite()
    }

    override fun createPresenter(): PersonDetailsPresenter {
        return PersonDetailsPresenter(this)
    }

    override fun showDetails(person: Person?) {
        hideMainLoading()
        person?.let {
            person_name.text = it.name
            person_birth.text = it.birthday
            person_known_for.text = it.known_for_department
            person_popularity.text = getString(R.string.popularity, it.popularity.toString())
            person_biography.text = it.biography
            Picasso.get()
                .load(ConfigInfo.imageUrl + it.profile_path)
                .into(person_poster)
            checkIfFavourite(it)
        }
    }

    private fun checkIfFavourite(movie: Person) {
        if (User.isFavourite(movie)){
            setFavourite()
        }
    }

    private fun setFavourite() {
        gif_image.playAnimation()
        btn_add_to_favourites.background = getDrawable(R.drawable.favourite_already)
        btn_add_to_favourites.text = getString(R.string.favourite_already)
        btn_add_to_favourites.isEnabled = false
    }

    private fun showPicturesFragment() {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("type", SearchResultItem.person)
        showFragment(PicturesFragment(), R.id.fragment_container, {}, bundle)
    }

    override fun showCredits(credits: ArrayList<PersonCredit.Credit>?) {
        adapter?.addAll(credits)
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
        Snackbar.make(root, message, Snackbar.LENGTH_LONG).show()
    }
}