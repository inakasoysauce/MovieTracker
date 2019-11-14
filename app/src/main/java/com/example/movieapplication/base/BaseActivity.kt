package com.example.movieapplication.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R

@SuppressLint("Registered")
abstract class BaseActivity<P : BasePresenter<V>, V : BaseView> : AppCompatActivity(), BaseFragmentListener, BaseView {

    private var loadingView: View? = null
    protected var presenter: P? = null
    protected var visibleFragment: SlidableFragment? = null

    private val fragments = ArrayList<SlidableFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    abstract fun createPresenter(): P

    override fun showMainLoading() {
        loadingView?.visibility = View.VISIBLE
        disableTouch()
    }

    override fun hideMainLoading() {
        loadingView?.visibility = View.GONE
        enableTouch()
    }

    protected fun setLoadingView(view: View) {
        loadingView = view
    }

    override fun showLoading() {
        showMainLoading()
    }

    override fun hideLoading() {
        hideMainLoading()
    }

    private fun enableTouch() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun disableTouch() {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    protected open fun showFragment(fragment: SlidableFragment, containerID: Int, runOnCommit: () -> Unit = {}, bundle: Bundle? = null) {
        if (bundle != null) {
            fragment.getFragment().arguments = bundle
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fragment.getEnterAnimation(), fragment.getExitAnimation())
        transaction.add(containerID, fragment.getFragment()).runOnCommit {
            runOnCommit()
        }.commit()
        fragments.add(fragment)
        visibleFragment = fragment
    }

    protected open fun hideFragment(fragment: SlidableFragment, runOnCommit: () -> Unit = {}) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(fragment.getEnterAnimation(), fragment.getExitAnimation())
        transaction.remove(fragment.getFragment())
            .runOnCommit {
                runOnCommit()
            }
            .commit()
        fragments.remove(fragment)
        if (fragments.isNotEmpty()) {
            visibleFragment = fragments.last()
        } else {
            visibleFragment = null
        }
    }

    protected open fun hideVisibleFragment() {
        visibleFragment?.let {
            hideFragment(it)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(it.rawX.toInt(), it.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun hideContainedFragment(fragment: SlidableFragment) {
        if (fragment == visibleFragment) {
            hideFragment(fragment)
        }
    }

    protected fun goBackToSearch() {
        MovieTracker.goBackToSearch()
    }

    override fun onBackPressed() {
        if (visibleFragment == null) {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            hideFragment(visibleFragment!!)
        }
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroyView()
    }
}