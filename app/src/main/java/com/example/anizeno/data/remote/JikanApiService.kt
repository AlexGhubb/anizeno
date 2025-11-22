package com.example.anizeno.data.remote

import com.example.anizeno.data.remote.dto.AnimeDetailResponse
import com.example.anizeno.data.remote.dto.AnimeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {
    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String
    ): AnimeSearchResponse
    @GET("seasons/now")
    suspend fun seasonList(): AnimeSearchResponse
    @GET("top/anime")
    suspend fun topList(): AnimeSearchResponse
    @GET("seasons/upcoming")
    suspend fun upcomingList(): AnimeSearchResponse
    @GET("anime")
    suspend fun searchAnimeByTitle(
        @Query("q") title: String
    ): AnimeSearchResponse
    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int
    ): AnimeDetailResponse
}