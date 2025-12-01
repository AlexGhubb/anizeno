package com.example.anizeno.data.local.room_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userEmail: String,
    val profileImageUri: String?,
    val userName: String?,
    val userBio: String?
)


