package com.example.movieapplication.receiver

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.movieapplication.event_bus.NetworkEvent
import com.example.movieapplication.network.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NetworkMonitor : ConnectivityManager.NetworkCallback() {
    private val networkRequest : NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    fun enable(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(networkRequest,this)
    }

    override fun onAvailable(network: Network) {
        runBlocking(Dispatchers.Main) {
            NetworkEvent.event(NetworkStatus.ONLINE)
        }
    }

    override fun onLost(network: Network) {
        runBlocking(Dispatchers.Main) {
            NetworkEvent.event(NetworkStatus.OFFLINE)
        }
    }
}