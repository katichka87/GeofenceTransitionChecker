package com.kateryna.testassignment.dagger

import com.kateryna.testassignment.Presenter
import com.kateryna.testassignment.adapters.GeofenceAdapter
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
    fun providePresenter(adapter: GeofenceAdapter): Presenter = Presenter(adapter)
}