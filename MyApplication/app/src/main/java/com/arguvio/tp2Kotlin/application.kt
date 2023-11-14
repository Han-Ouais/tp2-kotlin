package com.arguvio.tp2Kotlin

import android.app.Application
import android.content.Intent
import com.arguvio.tp2Kotlin.ui.activities.SplashActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ArguvioApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}