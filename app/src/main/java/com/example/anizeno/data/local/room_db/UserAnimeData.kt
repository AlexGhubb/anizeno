package com.example.anizeno.data.local.room_db


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "user_anime_list",
//    foreignKeys = [
//        ForeignKey(
//            entity = UserProfile::class,
//            parentColumns = ["email"],
//            childColumns = ["userEmail"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [
//        Index("userEmail"),
//        Index("malId"),
//        Index(value = ["userEmail", "malId", "category"], unique = true)
//    ]
)
data class UserAnimeData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val malId: Int?,
    val title: String,
    val imageUrl: String?,
    val category: String,
    val userEmail: String
): Parcelable