package com.example.anizeno.data.local.room_db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("anime_reviews",
//    foreignKeys = [
//        ForeignKey(
//            entity = UserProfile::class,
//            parentColumns = ["email"],
//            childColumns = ["userEmail"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [
//        Index("malId"),
//        Index("userEmail"),
//        Index(value = ["malId", "userEmail"], unique = true)
//    ]
)
data class AnimeReview(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val malId:Int,
    val title: String,
    val userEmail: String,
    val userName: String,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)


