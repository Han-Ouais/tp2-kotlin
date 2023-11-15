package com.arguvio.tp2Kotlin.api

import com.arguvio.tp2Kotlin.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    /**
     * Obtentir tous les utilisateurs
     */
    @GET("/users")
    fun getUsers(): Call<MutableList<User>>

    /**
     * Ajouter un nouvel utilisateur
     */
    @POST("/users")
    fun createUser(@Body newUser: User): Call<User>

    /**
     * Obtenir un utilisateur par son ID
     */
    @GET("/users/{id}")
    fun getUserById(@Path("id") userId: Int): Call<User>

    /**
     * Mettre à jour un utilisateur
     */
    @PUT("/users/{id}")
    fun updateUser(@Path("id") userId: Int, @Body user: User): Call<User>

    /**
     * Supprimer un utilisateur
     */
    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") userId: Int): Call<Void>
}