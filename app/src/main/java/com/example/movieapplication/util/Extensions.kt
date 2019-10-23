package com.example.movieapplication.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView


fun TextView.setTextWidthFade(value: String, duration: Long = 500L) {
    val animIn = AlphaAnimation(0.0f, 1.0f)
    animIn.duration = duration
    val animOut = AlphaAnimation(1.0f, 0.0f)
    animOut.duration = duration

    startAnimation(animOut)

    animOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            text = value
            startAnimation(animIn)
        }

        override fun onAnimationStart(animation: Animation?) {}
    })
}