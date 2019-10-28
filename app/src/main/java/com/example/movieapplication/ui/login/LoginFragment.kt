package com.example.movieapplication.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.movieapplication.MovieTracker
import com.example.movieapplication.R
import com.example.movieapplication.base.BaseFragment
import com.example.movieapplication.util.setTextWidthFade
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginPresenter>(), LoginView {

    private val login = 0
    private val register = 1
    private var state = login

    private var motionLayout: MotionLayout? = null

    private var listener: LoginFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MovieTracker.movieComponent.inject(this)
        Log.i("LoginFragment", "onCreate")
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as LoginFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        state = login
        bindViews()

        btn_login?.setOnClickListener {
            if (state == register) {
                switchToLogin()
            } else {
                attemptLogin()
            }
        }
        btn_register?.setOnClickListener {
            if (state == login) {
                switchToRegister()
            } else {
                attemptRegister()
            }
        }
    }

    private fun attemptLogin() {
        if (checkEditTextsLogin()) {
            showLoading()
            presenter?.attemptLogin(et_login_email.text.toString(), et_login_password.text.toString())
        }
    }

    private fun attemptRegister() {
        if (checkEditTextsRegister()) {
            showLoading()
            presenter?.attemptRegister(et_login_email.text.toString(), et_login_password.text.toString(), et_login_username.text.toString())
        }
    }

    override fun success() {
        hideLoading()
        listener?.loggedIn()
    }

    override fun showError(message: String) {
        hideLoading()
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    private fun switchToLogin() {
        motionLayout?.transitionToStart()
        tv_login_title.setTextWidthFade(getString(R.string.login), 250L)
        et_login_username.setText("")
        state = login
    }

    private fun switchToRegister() {
        motionLayout?.transitionToEnd()
        tv_login_title.setTextWidthFade(getString(R.string.register), 250L)
        state = register
    }

    private fun bindViews() {
        motionLayout = view as MotionLayout
    }

    private fun checkEditTextsLogin(): Boolean {
        return checkEditTextIsNotEmpty(et_login_email) && checkEditTextIsNotEmpty(et_login_password) && emailValid(et_login_email)
    }

    private fun checkEditTextsRegister(): Boolean {
        return checkEditTextsLogin() && checkEditTextIsNotEmpty(et_login_username)
    }

    private fun checkEditTextIsNotEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty()) {
            editText.error = "Field is empty"
            editText.requestFocus()
            false
        } else
            true
    }

    private fun emailValid(email: EditText): Boolean {
        return if (email.text.toString().contains('@')) {
            true
        } else {
            email.error = "Field must contain an '@' character"
            email.requestFocus()
            false
        }
    }

    override fun onDestroyView() {
        et_login_email.setText("")
        et_login_password.setText("")
        et_login_username.setText("")
        super.onDestroyView()
    }
}