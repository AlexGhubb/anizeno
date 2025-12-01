package com.example.anizeno.domain.repository

import com.example.anizeno.data.local.room_db.AnimeReview

interface ReviewRepository{
    suspend fun addReview(review: AnimeReview)
    suspend fun deleteReview(review: AnimeReview)
    suspend fun getReviewsForAnime(malId: Int): List<AnimeReview>

    suspend fun hasUserReviewed(malId: Int, userEmail: String): Boolean

}






