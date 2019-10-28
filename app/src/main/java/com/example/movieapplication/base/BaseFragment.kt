package com.example.movieapplication.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment<P : BasePresenter> : Fragment(), BaseView {

    private var listener: BaseFragmentListener? = null

    protected var presenter : P? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as BaseFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = createPresenter()
    }

    abstract fun createPresenter() : P

    protected fun showLoading() {
        listener?.showMainLoading()
    }

    protected fun hideLoading() {
        listener?.hideMainLoading()
    }
}