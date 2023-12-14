package com.arguvio.tp2Kotlin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité User pour la base de données cache Room
 */
@Entity
data class UserEntity(
    @PrimaryKey val _id: String? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "password") val password: String? = null
)

/**
 * Modèle de données de User
 */
data class User(
    val _id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)