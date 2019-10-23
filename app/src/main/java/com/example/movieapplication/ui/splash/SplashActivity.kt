package com.example.movieapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapplication.R
import com.example.movieapplication.ui.search.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activtiy_splash.*

class SplashActivity : AppCompatActivity(), SplashView {

    private var presenter = SplashPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activtiy_splash)
        presenter.addView(this)
        presenter.tryAutoLogin()
    }

    override fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(main_layout, message, Snackbar.LENGTH_LONG).show()
    }
}
