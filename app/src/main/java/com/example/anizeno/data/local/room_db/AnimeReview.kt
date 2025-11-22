package com.example.anizeno.data.local.room_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("anime_reviews")
data class AnimeReview(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val malId:Int,
    val title: String,
    val userEmail: String,
    val userName: String,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)


