package com.example.anizeno.data.repository

import com.example.anizeno.data.local.room_db.UserAnimeData
import com.example.anizeno.data.local.room_db.UserAnimeDataDao
import com.example.anizeno.domain.repository.UserAnimeDataRepository

class UserAnimeDataRepositoryImpl(private val dao: UserAnimeDataDao) : UserAnimeDataRepository {

    override suspend fun addAnime(anime: UserAnimeData) = dao.insertAnime(anime)

    override suspend fun getCategory(category: String, userEmail: String) =
        dao.getAnimesByCategory(category, userEmail)

    override suspend fun removeAnime(anime: UserAnimeData) = dao.deleteAnime(anime)

    override suspend fun existsInCategory(malId: Int, category: String, userEmail: String): Boolean {
        return dao.existsInCategory(malId, category, userEmail) > 0
    }

    override suspend fun updateAnime(anime: UserAnimeData) {
        dao.updateAnime(anime)
    }
}