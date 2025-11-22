package com.example.anizeno.data.local.room_db

import androidx.room.*

import kotlinx.coroutines.flow.Flow

@Dao
interface UserAnimeDataDao {
    @Query("SELECT * FROM user_anime_list WHERE category = :category AND userEmail = :userEmail")
    suspend fun getAnimesByCategory(category: String, userEmail: String): List<UserAnimeData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: UserAnimeData)

    @Delete
    suspend fun deleteAnime(anime: UserAnimeData)

    @Query("""SELECT COUNT(*) FROM user_anime_list WHERE title = :title AND category = :category AND userEmail = :userEmail""")
    suspend fun existsAnime(title: String, category: String, userEmail: String): Int

    @Query("SELECT COUNT(*) FROM user_anime_list WHERE malId = :malId AND category = :category AND userEmail = :userEmail")
    suspend fun existsInCategory(malId: Int, category: String, userEmail: String): Int

    @Update
    suspend fun updateAnime(anime: UserAnimeData)
}