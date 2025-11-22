package com.example.anizeno.data.local.room_db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.firestore.core.UserData


@Database(entities = [UserAnimeData::class,UserProfile::class, AnimeReview::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userAnimeDataDao(): UserAnimeDataDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun animeReviewDao(): AnimeReviewDao


    //@SuppressLint("StaticFieldLeak")

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "anime_local_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}