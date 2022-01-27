package com.pomeloassignment.android

import android.app.Application
import com.pomeloassignment.android.di.AppModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppModule.application = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}