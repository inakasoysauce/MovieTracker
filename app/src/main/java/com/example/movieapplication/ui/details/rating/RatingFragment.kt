package com.example.movieapplication.ui.details.rating

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_rating.*

class RatingFragment : BaseFragment<RatingPresenter, RatingView>(), RatingView {

    private var listener: RatingFragmentListener? = null

    interface RatingFragmentListener {
        fun onCancel()
        fun onRatingGiven(rating: Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as RatingFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents()
    }

    override fun initComponents() {
        initButtons()
    }

    private fun initButtons() {
        btn_cancel.setOnClickListener {
            listener?.onCancel()
        }
        btn_ok.setOnClickListener {
            val rating = (rating_bar.rating * 2).toInt()
            presenter?.giveRating(arguments?.getString("id"), arguments?.getString("type"), rating)
        }
    }


    override fun success(rating: Int) {
        listener?.onRatingGiven(rating)
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_bottom
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_bottom
    }

    override fun createPresenter(): RatingPresenter {
        return RatingPresenter(this)
    }
}