package com.example.anizeno.data.repository

import com.example.anizeno.data.local.room_db.AnimeReview
import com.example.anizeno.data.local.room_db.AnimeReviewDao
import com.example.anizeno.domain.repository.ReviewRepository

class ReviewRepositoryImpl(private val dao: AnimeReviewDao): ReviewRepository {
    override suspend fun addReview(review: AnimeReview) = dao.insertReview(review)
    override suspend fun deleteReview(review: AnimeReview) = dao.deleteReview(review)

    override suspend fun getReviewsForAnime(malId:Int): List<AnimeReview> = dao.getReviewsForAnime(malId)
    override suspend fun hasUserReviewed(malId: Int, userEmail: String): Boolean {
        return dao.hasUserReviewed(malId, userEmail) > 0
    }
}




