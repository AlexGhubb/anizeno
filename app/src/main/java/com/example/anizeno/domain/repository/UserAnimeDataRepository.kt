package com.example.anizeno.domain.repository

import com.example.anizeno.data.local.room_db.UserAnimeData


interface UserAnimeDataRepository {
    suspend fun addAnime(anime: UserAnimeData)
    suspend fun removeAnime(anime: UserAnimeData)

    /**
     * Devuelve la lista de animes de una categoría para un usuario.
     * category: "fav" | "vista" | "me_interesa"
     */
    suspend fun getCategory(category: String, userEmail: String): List<UserAnimeData>

    /**
     * Comprueba si existe un anime en una categoría para un usuario.
     */
    suspend fun existsInCategory(malId: Int, category: String, userEmail: String): Boolean

    suspend fun updateAnime(anime: UserAnimeData)
}