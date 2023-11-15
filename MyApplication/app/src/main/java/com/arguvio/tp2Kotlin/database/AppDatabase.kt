package com.arguvio.tp2Kotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arguvio.tp2Kotlin.dao.UserDao
import com.arguvio.tp2Kotlin.models.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}