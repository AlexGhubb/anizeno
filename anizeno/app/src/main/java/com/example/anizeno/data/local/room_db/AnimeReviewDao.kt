package com.example.anizeno.data.local.room_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnimeReviewDao{
    /**
     * Inserts a new review into the database.
     *
     * @param review The review to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: AnimeReview)

    /**
     * Retrieves all reviews for a specific anime based on its MAL ID.
     *
     * @param malId The MAL ID of the anime.
     */
    @Query("SELECT * FROM anime_reviews WHERE malId = :malId ORDER BY timestamp DESC")
    suspend fun getReviewsForAnime(malId: Int): List<AnimeReview>

    /**
     * Retrieves all reviews for a specific user based on their email.
     *
     * @param userEmail The email of the user.
     */
    @Query("DELETE FROM anime_reviews WHERE id = :reviewId")
    suspend fun deleteReview(reviewId: Int)

    /**
     * Deletes all reviews associated with a specific user.
     *
     * @param userEmail The email of the user.
     */
    @Query("DELETE FROM anime_reviews WHERE userEmail = :userEmail")
    suspend fun deleteReviewsByUser(userEmail: String)

    /**
     * Checks if a user has already reviewed a specific anime.
     *
     * @param malId The MAL ID of the anime.
     */
    @Query("SELECT COUNT(*) FROM anime_reviews WHERE malId = :malId AND userEmail = :userEmail")
    suspend fun hasUserReviewed(malId: Int, userEmail: String): Int

    /**
     * Deletes a specific review from the database.
     *
     * @param review The review to be deleted.
     */
    @Delete
    suspend fun deleteReview(review: AnimeReview)

    /**
     * Updates the user name for a specific user.
     *
     * @param userEmail The email of the user.
     * @param newName The new user name.
     */
    @Query("UPDATE anime_reviews SET userName = :newName WHERE userEmail = :userEmail")
    suspend fun updateUserNameForEmail(userEmail: String, newName: String)

}