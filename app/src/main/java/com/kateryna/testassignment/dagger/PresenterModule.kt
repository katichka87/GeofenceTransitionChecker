package com.kateryna.testassignment.dagger

import com.kateryna.testassignment.view.Presenter
import com.kateryna.testassignment.adapters.GeofenceAdapter
import com.kateryna.testassignment.adapters.WiFiStateAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by kati4ka on 1/16/18.
 */
@Module
class PresenterModule {
    @Provides
    @Singleton
    fun providePresenter(adapter: GeofenceAdapter, wiFiStateAdapter: WiFiStateAdapter): Presenter = Presenter(adapter, wiFiStateAdapter)
}