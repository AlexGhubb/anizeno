package com.example.anizeno.data.local.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserProfileDao {

    @Query("SELECT * FROM user_profile WHERE userEmail = :email LIMIT 1")
    suspend fun getUserProfile(email: String): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("DELETE FROM user_profile WHERE userEmail = :email")
    suspend fun deleteProfile(email: String)

    @Query("UPDATE user_profile SET userName = :userName, userBio = :bio WHERE userEmail = :email")
    suspend fun updateUserInfo(email: String, userName: String, bio: String)


}