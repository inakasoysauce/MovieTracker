package com.example.movieapplication.event_bus

import com.example.movieapplication.network.NetworkStatus
import com.example.movieapplication.util.NotificationEvent
import kotlinx.event.event

class Events {
    companion object {
        val networkEvent = event<NetworkStatus>()
        val friendEvent = event<NotificationEvent>()
    }
}