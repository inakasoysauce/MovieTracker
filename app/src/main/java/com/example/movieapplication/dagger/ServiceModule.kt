package com.example.movieapplication.dagger

import com.example.movieapplication.event_bus.EventBus
import com.example.movieapplication.network.client.ApiClient
import com.example.movieapplication.receiver.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


@Module
class ServiceModule {

    @Provides
    fun provideApiClient() : ApiClient = ApiClient()

    @Provides
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    fun provideEventBus() : EventBus = EventBus()

    @Provides
    fun provideNetworkMonitor() : NetworkMonitor = NetworkMonitor()
}