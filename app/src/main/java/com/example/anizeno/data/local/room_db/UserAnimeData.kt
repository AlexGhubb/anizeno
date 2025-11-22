package com.example.anizeno.data.local.room_db


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_anime_list")
data class UserAnimeData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val malId: Int?,
    val title: String,
    val imageUrl: String?,
    val category: String,
    val userEmail: String
): Parcelable