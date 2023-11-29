package com.arguvio.tp2Kotlin

import android.app.Application
import android.content.Intent
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.arguvio.tp2Kotlin.ui.activities.SplashActivity
import com.arguvio.tp2Kotlin.workers.UpdateCacheWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class ArguvioApplication : Application() {
    override fun onCreate() {
        super.onCreate()

    }

    private fun setupPeriodicWork() {
        val updateWorkRequest = PeriodicWorkRequestBuilder<UpdateCacheWorker>(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueue(updateWorkRequest)
    }
}