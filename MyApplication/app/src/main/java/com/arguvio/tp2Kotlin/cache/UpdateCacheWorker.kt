package com.arguvio.tp2Kotlin.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arguvio.tp2Kotlin.repository.UserRepository

class UpdateCacheWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val userRepository: UserRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            userRepository.refreshUsers()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}