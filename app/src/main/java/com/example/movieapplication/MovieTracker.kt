package com.example.movieapplication

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.example.movieapplication.dagger.AppComponent
import com.example.movieapplication.dagger.DaggerAppComponent
import com.example.movieapplication.dagger.ServiceModule
import com.example.movieapplication.ui.search.SearchActivity
import com.example.movieapplication.ui.splash.SplashActivity
import java.util.*

class MovieTracker : Application() {

    companion object {
        lateinit var movieComponent: AppComponent

        val activityList = Stack<Activity>()

        fun goBackToSearch() {
            activityList.forEach {
                if (it !is SearchActivity) {
                    it.finish()
                } else {
                    it.shouldCloseFavourites = true
                }
            }
        }
    }

    init {
        try {
            movieComponent = initDagger()
        } catch (e: Exception) {
            e.toString()
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityDestroyed(activity: Activity) {
                activityList.remove(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                if (activity !is SplashActivity)
                    activity.setTheme(R.style.AppTheme_NoActionBar)
                activityList.push(activity)
            }
        })
    }




    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
            .serviceModule(ServiceModule())
            .build()
}