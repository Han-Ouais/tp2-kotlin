package com.arguvio.tp2Kotlin.OnlineSource

import com.arguvio.tp2Kotlin.api.ApiService
import com.arguvio.tp2Kotlin.models.User
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class OnlineSource @Inject constructor(private val apiService: ApiService){
    suspend fun getUsers(): List<User>? {
        try {
            val call: Call<MutableList<User>> = apiService.getUsers()
            val response: Response<MutableList<User>> = call.execute()

            if (response.isSuccessful) {
                return response.body()
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }
}