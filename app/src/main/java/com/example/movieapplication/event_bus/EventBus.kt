package com.example.movieapplication.event_bus

import com.example.movieapplication.network.NetworkStatus
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class EventBus {

    private val networkStatusProcessor = BehaviorProcessor.create<NetworkStatus>()

    val networkStatus: Flowable<NetworkStatus>
        get() = networkStatusProcessor

    fun setNetworkStatus(status: NetworkStatus) {
        networkStatusProcessor.onNext(status)
    }
}