package com.arguvio.tp2Kotlin.repository

import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.dao.UserDao
import com.arguvio.tp2Kotlin.models.User
import com.arguvio.tp2Kotlin.models.UserEntity
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
        val createdUser = onlineSource.createUser(newUser)
        createdUser?.let {
            userDao.insert(it.toEntity())
        }
        return createdUser
    }

    /**
     * Récupère un utilisateur via son identifiant, indiqué en paramètre
     */
    suspend fun getUserById(userId: Int): User? {
        return onlineSource.getUserById(userId)
    }

    /**
     * Met à jour un utilisateur, dont l'identifiant est en paramètre
     */
    suspend fun updateUser(userId: Int, updatedUser: User): User? {
        val updatedUserFromApi = onlineSource.updateUser(userId, updatedUser)
        updatedUserFromApi?.let {
            userDao.insert(it.toEntity())
        }
        return updatedUserFromApi
    }

    /**
     * Supprime un utilisateur dont l'identifiant est en paramètre
     */
    suspend fun deleteUser(userId: Int): Boolean {
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
     * Convertie un UserEntity en User
     */
    private fun UserEntity.toUser(): User {
        return User(
            _id = this._id,
            name = this.name,
            email = this.email,
        )
    }
}