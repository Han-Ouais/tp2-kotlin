package com.arguvio.tp2Kotlin.repository

import android.util.Log
import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.dao.UserDao
import com.arguvio.tp2Kotlin.models.User
import com.arguvio.tp2Kotlin.models.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(private val onlineSource: OnlineSource, private val userDao: UserDao) {

    /**
     * Récupère tous les utilisateurs dans le cache si la database n'est pas vide, sinon récupère
     * les utilisateurs via l'API REST et injecte les données dans la database Room en cache
     */
    suspend fun getUsers(): List<User>? {
        val cachedUsers = userDao.getAll()

        /* Si les utilisateurs ne sont pas en cache, on les charge via l'API REST */
        if (cachedUsers.isEmpty()) {
            val usersFromApi = onlineSource.getUsers()
            usersFromApi?.let {
                val userEntities = it.map { user -> user.toEntity() }
                userDao.insert(*userEntities.toTypedArray())
                return it
            }
        } else { // sinon on les charge via le cache
            return cachedUsers.map { userEntity -> userEntity.toUser() }
        }

        return null // On retourne une valeur nulle si ni le cache ni l'API n'ont des données
    }

    /**
     * Crée un nouvel utilisateur
     */
    suspend fun createUser(newUser: User): User? {
        return try {
            val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(newUser.email!!, newUser.password!!).await()
            newUser.copy(_id = authResult.user?.uid)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error creating user", e)
            null
        }
    }

    /**
     * Récupère un utilisateur via son identifiant, indiqué en paramètre
     */
    suspend fun getUserById(userId: String): User? {
        val db = FirebaseFirestore.getInstance()
        return try {
            val documentSnapshot = db.collection("users").document(userId).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user", e)
            null
        }
    }

    /**
     * Met à jour un utilisateur, dont l'identifiant est en paramètre
     */
    suspend fun updateUserProfile(user: User) {
        val db = FirebaseFirestore.getInstance()
        val userProfile = hashMapOf(
            "name" to user.name,
            "email" to user.email
            // autres champs...
        )
        user._id?.let { userId ->
            db.collection("users").document(userId).set(userProfile)
                .addOnSuccessListener { Log.d("UserRepository", "User profile updated") }
                .addOnFailureListener { e -> Log.w("UserRepository", "Error updating profile", e) }
        }
    }

    /**
     * Supprime un utilisateur dont l'identifiant est en paramètre
     */
    suspend fun deleteUser(userId: String): Boolean {
        val isDeleted = onlineSource.deleteUser(userId)
        if (isDeleted) {
            userDao.delete(UserEntity(_id = userId))
        }
        return isDeleted
    }

    /**
     * Convertie un User en UserEntity
     */
    private fun User.toEntity(): UserEntity {
        return UserEntity(
            _id = this._id,
            name = this.name,
            email = this.email,
        )
    }

    /**
     * Convertit un UserEntity en User
     */
    private fun UserEntity.toUser(): User {
        return User(
            _id = this._id,
            name = this.name,
            email = this.email,
        )
    }

    /**
     * Rafraîchit la liste des utilisateurs en la récupérant depuis l'API REST et en mettant à jour
     * la base de données locale Room.
     */
    suspend fun refreshUsers() {
        val usersFromApi = onlineSource.getUsers()
        usersFromApi?.let {
            val userEntities = it.map { user -> user.toEntity() }
            userDao.insert(*userEntities.toTypedArray())
        }
    }
}