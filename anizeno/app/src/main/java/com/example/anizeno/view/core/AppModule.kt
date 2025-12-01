package com.example.anizeno.view.core

import android.app.Application
import com.example.anizeno.App

import com.example.anizeno.data.repository.ReviewRepositoryImpl
import com.example.anizeno.data.repository.UserAnimeDataRepositoryImpl
import com.example.anizeno.domain.repository.ReviewRepository
import com.example.anizeno.domain.repository.UserAnimeDataRepository
import com.example.anizeno.domain.repository.UserProfileRepository

object AppModule {

    lateinit var userAnimeRepo: UserAnimeDataRepository
        private set

    lateinit var userProfileRepo: UserProfileRepository
        private set

    lateinit var reviewRepo: ReviewRepository
        private set

    fun initialize(app: Application) {

        val db = App.database

        userAnimeRepo = UserAnimeDataRepositoryImpl(db.userAnimeDataDao())
        reviewRepo = ReviewRepositoryImpl(db.animeReviewDao())
//        userProfileRepo = UserProfileRepositoryImpl(
//            db.userProfileDao(),
//            db.animeReviewDao()
//        )
    }
}