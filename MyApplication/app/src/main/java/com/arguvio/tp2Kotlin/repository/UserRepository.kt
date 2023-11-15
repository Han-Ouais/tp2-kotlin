package com.arguvio.tp2Kotlin.repository

import com.arguvio.tp2Kotlin.OnlineSource.OnlineSource
import com.arguvio.tp2Kotlin.dao.UserDao
import com.arguvio.tp2Kotlin.models.User
import com.arguvio.tp2Kotlin.models.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val onlineSource: OnlineSource, private val userDao: UserDao) {

    suspend fun getUsers(): List<User>? {
        val cachedUsers = userDao.getAll()

        if (cachedUsers.isEmpty()) {
            val usersFromApi = onlineSource.getUsers()
            usersFromApi?.let {
                val userEntities = it.map { user -> user.toEntity() }
                userDao.insert(*userEntities.toTypedArray())
                return it
            }
        } else {
            return cachedUsers.map { userEntity -> userEntity.toUser() }
        }

        return null // Retourner null si ni le cache ni l'API n'ont des données
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(
            _id = this._id,
            name = this.name,
            email = this.email,
        )
    }

    private fun UserEntity.toUser(): User {
        return User(
            _id = this._id,
            name = this.name,
            email = this.email,
        )
    }
}