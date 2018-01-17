package com.kateryna.testassignment.dagger

import com.kateryna.testassignment.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by kati4ka on 1/16/18.
 */
@Singleton
@Component(modules = [AppModule::class, PresenterModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
}