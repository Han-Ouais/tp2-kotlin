package com.arguvio.tp2Kotlin.OnlineSource

import android.util.Log
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
                val userList: List<User>? = response.body()
                if (userList != null) {
                    for (user in userList) {
                        Log.d(
                            "OnlineSource",
                            "User : ${user._id} | Name : ${user.name} | Email : ${user.email} | Password : ${user.password}"
                        )
                    }
                    return response.body()
                }
            } else {
                Log.d(
                    "OnlineSource",
                    "NULL")
                return null
            }
        } catch (e: Exception) {
            Log.d(
                "OnlineSource",
                "NULL")
            return null
        }
        Log.d(
            "OnlineSource",
            "NULL")
        return null
    }
}