package com.example.movieapplication.base

import android.annotation.SuppressLint
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), BaseFragmentListener {

    private var loadingView : View? = null

    override fun showMainLoading() {
        loadingView?.visibility = View.VISIBLE
        disableTouch()
    }

    override fun hideMainLoading() {
        loadingView?.visibility = View.GONE
        enableTouch()
    }

    protected fun setLoadingView(view : View) {
        loadingView = view
    }

    private fun enableTouch() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun disableTouch() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}