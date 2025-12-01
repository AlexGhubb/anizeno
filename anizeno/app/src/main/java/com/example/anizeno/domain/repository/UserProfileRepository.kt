package com.example.anizeno.domain.repository

import com.example.anizeno.data.local.room_db.UserProfile

interface UserProfileRepository{
    suspend fun getUserProfile(userEmail: String): UserProfile?
    suspend fun insertOrUpdateProfile(userProfile: UserProfile)
    suspend fun updateProfileImage(userEmail: String, imageUrl: String)

    suspend fun updateUserNameInReviews(userEmail: String, newName: String)

}