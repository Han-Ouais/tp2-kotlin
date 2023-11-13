package com.arguvio.tp2Kotlin.repository

import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.models.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val onlineSource: OnlineSource) {
    suspend fun getUsers(): List<User>? {
        return onlineSource.getUsers()
    }
}