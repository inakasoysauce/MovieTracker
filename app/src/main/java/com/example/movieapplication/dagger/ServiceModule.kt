package com.example.movieapplication.dagger

import com.example.movieapplication.network.client.ApiClient
import com.example.movieapplication.receiver.NetworkMonitor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class ServiceModule {

    @Provides
    fun provideApiClient() : ApiClient = ApiClient()

    @Provides
    fun provideCompositeDisposable() : CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideNetworkMonitor() : NetworkMonitor = NetworkMonitor()
}