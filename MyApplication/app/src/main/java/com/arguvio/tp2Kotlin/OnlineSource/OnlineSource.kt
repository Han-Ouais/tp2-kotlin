package com.arguvio.tp2Kotlin.OnlineSource

import android.util.Log
import com.arguvio.tp2Kotlin.api.ApiService
import com.arguvio.tp2Kotlin.models.User
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class OnlineSource @Inject constructor(private val apiService: ApiService){

    /**
     * Récupère tous les utilisateurs
     */
    suspend fun getUsers(): List<User>? {
        try {
            val call: Call<MutableList<User>> = apiService.getUsers()
            val response: Response<MutableList<User>> = call.execute()

            /* Si les données sont bien reçues */
            if (response.isSuccessful) {
                val userList: List<User>? = response.body()

                /* Si la liste des utilisateurs n'est pas nulle */
                if (userList != null) {
                    for (user in userList) {
                        Log.d(
                            "OnlineSource",
                            "User : ${user._id} | Name : ${user.name} | Email : ${user.email} | Password : ${user.password}"
                        )
                    }
                    return userList // Retour de la liste d'utilisateurs
                }
            } else {
                Log.d(
                    "OnlineSource",
                    "NULL")
                return null // sinon on retourne une valeur nulle
            }
        } catch (e: Exception) {
            Log.d(
                "OnlineSource",
                "NULL")
            return null // retour de valeur nulle en cas d'erreur
        }
        Log.d(
            "OnlineSource",
            "NULL")
        return null // retour de valeur nulle en cas d'erreur
    }

    /**
     * Ajoute un nouvel utilisateur
     */
    suspend fun createUser(newUser: User): User? {
        try {
            val call: Call<User> = apiService.createUser(newUser)
            val response: Response<User> = call.execute()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("OnlineSource", "Failed to create user")
            }
        } catch (e: Exception) {
            Log.e("OnlineSource", "Exception: ${e.message}")
        }
        return null
    }

    /**
     * Récupère un utilisateur grâce à son identifiant indiqué en paramètre
     */
    suspend fun getUserById(userId: Int): User? {
        try {
            val call: Call<User> = apiService.getUserById(userId)
            val response: Response<User> = call.execute()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("OnlineSource", "Failed to get user by ID")
            }
        } catch (e: Exception) {
            Log.e("OnlineSource", "Exception: ${e.message}")
        }
        return null
    }

    /**
     * Met à jour un utilisateur, indiqué par son identifiant en paramètre
     */
    suspend fun updateUser(userId: Int, updatedUser: User): User? {
        try {
            val call: Call<User> = apiService.updateUser(userId, updatedUser)
            val response: Response<User> = call.execute()
            if (response.isSuccessful) {
                return response.body()
            } else {
                Log.e("OnlineSource", "Failed to update user")
            }
        } catch (e: Exception) {
            Log.e("OnlineSource", "Exception: ${e.message}")
        }
        return null
    }

    /**
     * Supprime un utilisateur en fonction de son identifiant
     */
    suspend fun deleteUser(userId: Int): Boolean {
        try {
            val call: Call<Void> = apiService.deleteUser(userId)
            val response: Response<Void> = call.execute()
            return response.isSuccessful
        } catch (e: Exception) {
            Log.e("OnlineSource", "Exception: ${e.message}")
        }
        return false
    }
}