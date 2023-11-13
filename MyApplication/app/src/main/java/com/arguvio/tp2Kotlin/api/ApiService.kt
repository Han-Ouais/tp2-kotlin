package com.arguvio.tp2Kotlin.api

import com.arguvio.tp2Kotlin.models.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/users")
    fun getUsers(): Call<MutableList<User>>
}