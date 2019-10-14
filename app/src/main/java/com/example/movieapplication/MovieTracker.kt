package com.example.movieapplication

import android.app.Activity
import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.example.movieapplication.dagger.AppComponent
import com.example.movieapplication.dagger.DaggerAppComponent
import com.example.movieapplication.dagger.PresenterModule
import com.example.movieapplication.dagger.ServiceModule

class MovieTracker : Application() {

    companion object {
        lateinit var movieComponent: AppComponent
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

            override fun onActivityDestroyed(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        })
    }


    private fun initDagger(): AppComponent =
        DaggerAppComponent.builder()
            .presenterModule(PresenterModule(this))
            .serviceModule(ServiceModule())
            .build()
}