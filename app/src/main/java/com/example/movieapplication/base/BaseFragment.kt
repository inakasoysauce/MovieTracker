package com.example.movieapplication.base

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment

abstract class BaseFragment<P : BasePresenter<V>, V : BaseView> : Fragment(), BaseView, SlidableFragment {

    private var listener: BaseFragmentListener? = null

    protected var presenter: P? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as BaseFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = createPresenter()
    }

    abstract fun createPresenter(): P

    override fun showLoading() {
        listener?.showMainLoading()
    }

    override fun hideLoading() {
        listener?.hideMainLoading()
    }

    override fun getFragment(): Fragment {
        return this
    }

    protected fun hideMe() {
        listener?.hideContainedFragment(this)
    }

    protected fun checkEditTextIsNotEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty()) {
            editText.error = "Field is empty"
            editText.requestFocus()
            false
        } else
            true
    }

    override fun showError(message: String) {
        listener?.showError(message)
    }
}