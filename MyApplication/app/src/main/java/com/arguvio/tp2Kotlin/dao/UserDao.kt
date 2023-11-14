package com.arguvio.tp2Kotlin.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arguvio.tp2Kotlin.models.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: List<UserEntity>)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM userentity")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM userentity WHERE userentity._id = :userId")
    fun getUserById(userId: Int): UserEntity
}