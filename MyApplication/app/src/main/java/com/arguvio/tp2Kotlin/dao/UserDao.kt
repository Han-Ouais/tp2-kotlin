package com.arguvio.tp2Kotlin.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arguvio.tp2Kotlin.models.UserEntity

@Dao
interface UserDao {
    /**
     * Injecte un nouvel utilisateur dans la base de données cache
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<UserEntity>)

    /**
     * Supprime un utilisateur de la base de données cache
     */
    @Delete
    fun delete(user: UserEntity)

    /**
     * Renvoie tous les utilisateurs de la base de données cache
     */
    @Query("SELECT * FROM userentity")
    fun getAll(): List<UserEntity>

    /**
     * Renvoie l'utilisateur présent dans la base de données cache qui a l'id indiqué en paramètre
     */
    @Query("SELECT * FROM userentity WHERE userentity._id = :userId")
    fun getUserById(userId: Int): UserEntity
}