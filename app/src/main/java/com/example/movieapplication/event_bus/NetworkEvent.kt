package com.example.movieapplication.event_bus

import com.example.movieapplication.network.NetworkStatus
import kotlinx.event.event

class NetworkEvent {
    companion object {
        val event = event<NetworkStatus>()
    }
}