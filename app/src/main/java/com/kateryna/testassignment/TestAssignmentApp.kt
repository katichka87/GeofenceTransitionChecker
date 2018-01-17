package com.kateryna.testassignment

import android.app.Application
import com.kateryna.testassignment.dagger.AppComponent
import com.kateryna.testassignment.dagger.AppModule
import com.kateryna.testassignment.dagger.DaggerAppComponent

/**
 * Created by kati4ka on 1/16/18.
 */

class TestAssignmentApp: Application() {
    lateinit var appComponent: AppComponent

    private fun initDagger(app: TestAssignmentApp): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }
}
