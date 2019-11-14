package com.example.movieapplication.ui.profile.password_change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_new_password.*

class PasswordChangeFragment : BaseFragment<PasswordChangePresenter, PasswordChangeView>(), PasswordChangeView {

    override fun createPresenter(): PasswordChangePresenter {
        return PasswordChangePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_ok.setOnClickListener {
            changePassword()
        }
        btn_cancel.setOnClickListener {
            hideMe()
        }
    }

    private fun changePassword() {
        if (checkEditTextIsNotEmpty(et_new_password)) {
            showLoading()
            presenter?.changePassword(et_new_password.text.toString())
        }
    }

    override fun success() {
        hideLoading()
        hideMe()
    }

    override fun getEnterAnimation(): Int {
        return R.anim.slide_in_bottom
    }

    override fun getExitAnimation(): Int {
        return R.anim.slide_out_bottom
    }

    override fun showError(message: String) {
        super.showError(message)
        hideLoading()
    }
}