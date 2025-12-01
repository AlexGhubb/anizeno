package com.example.anizeno.view.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.anizeno.data.local.room_db.AnimeReview
import com.example.anizeno.data.local.room_db.AppDatabase
import com.example.anizeno.data.repository.ReviewRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing anime reviews.
 */
class ReviewViewModel (application: Application): AndroidViewModel(application){
    private val reviewDao = AppDatabase.Companion.getInstance(application).animeReviewDao()
    private val repo= ReviewRepositoryImpl(reviewDao)

    private val _reviews = MutableStateFlow<List<AnimeReview>>(emptyList())
    val reviews = _reviews.asStateFlow()

    /**
     * Loads reviews for a specific anime using the provided MAL ID.
     */
    fun loadReviewsForAnime(malId: Int){
        viewModelScope.launch {
            _reviews.value =  repo.getReviewsForAnime(malId)

        }
    }

    /**
     * Adds a new review to the database.
     */
    fun addReview(review: AnimeReview){
        viewModelScope.launch {
            repo.addReview(review)
            _reviews.value= repo.getReviewsForAnime(review.malId)

        }

    }

    /**
     * Checks if a user has already reviewed a specific anime using the provided MAL ID and email.
     */
    suspend fun hasUserReviewed(malId: Int, userEmail: String): Boolean {
        return reviewDao.hasUserReviewed(malId, userEmail) > 0
    }

    /**
     * Deletes a review from the database.
     */
    fun deleteReview(review: AnimeReview) {
        viewModelScope.launch {
            reviewDao.deleteReview(review)
            _reviews.value = reviewDao.getReviewsForAnime(review.malId)
        }
    }

    /**
     * Updates the user name for a specific user email.
     */
    fun updateUserNameForUser(userEmail: String, newName: String) {
        viewModelScope.launch {
            reviewDao.updateUserNameForEmail(userEmail, newName)
        }
    }

}