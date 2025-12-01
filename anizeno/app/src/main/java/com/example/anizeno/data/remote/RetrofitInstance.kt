package com.example.anizeno.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val apiSearch: JikanApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanApiService::class.java)
    }
//    val apiSeasonNowList: JikanApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.jikan.moe/v4/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(JikanApiService::class.java)
//    }
//    val apiTopAnimeList: JikanApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.jikan.moe/v4/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(JikanApiService::class.java)
//    }
//    val apiUpcomingAnimeList: JikanApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl("https://api.jikan.moe/v4/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(JikanApiService::class.java)
//    }
}