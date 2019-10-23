package com.example.movieapplication.base

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var listener : BaseFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as BaseFragmentListener
    }

    protected fun showLoading() {
        view?.alpha = 0.3f
        listener?.showMainLoading()
    }

    protected fun hideLoading() {
        view?.alpha = 1f
        listener?.hideMainLoading()
    }
}