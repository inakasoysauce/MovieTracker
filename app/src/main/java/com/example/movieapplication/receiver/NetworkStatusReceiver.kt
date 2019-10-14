package com.example.movieapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.movieapplication.event_bus.NetworkEvent
import com.example.movieapplication.network.NetworkStatus

class NetworkStatusReceiver() : BroadcastReceiver() {

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val cManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cManager.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected) {
                NetworkEvent.event(NetworkStatus.ONLINE)
            } else {
                NetworkEvent.event(NetworkStatus.OFFLINE)
            }
        }
    }
}