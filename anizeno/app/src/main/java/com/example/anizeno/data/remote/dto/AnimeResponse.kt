package com.example.anizeno.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AnimeSearchResponse(
    val data: List<AnimeData>
)
data class AnimeDetailResponse(
    val data: AnimeData
)
@Parcelize
data class AnimeData(
    @SerializedName("mal_id") val malId: Int,
    val title: String?,
    val titles: List<AnimeTitle>?,
    val images: AnimeImages?,
    val score: Double?,
    val status: String?,
    val episodes: Int?,
    val year: Int?,
    val studios: List<AnimeStudio>?,
    val synopsis: String?,
    val genres: List<AnimeGenres>?,
    val trailer: AnimeTrailer?
) : Parcelable {
    fun getTitleDefault() = titles?.firstOrNull { it.type == "Default" }?.title ?: "_"
    fun getTitleJapanese() = titles?.firstOrNull { it.type == "Japanese" }?.title ?: "_"
    fun getTitleEnglish() = titles?.firstOrNull { it.type == "English" }?.title ?: "_"
    fun getTitleSpanish() = titles?.firstOrNull { it.type == "Spanish" }?.title ?: "_"
}

@Parcelize
data class AnimeTitle(
    val type: String?,
    val title: String?
) : Parcelable

@Parcelize
data class AnimeGenres(
    val name: String?
) : Parcelable

@Parcelize
data class AnimeImages(
    val jpg: AnimeJpg? = null
) : Parcelable


@Parcelize
data class AnimeStudio(
    val name: String?
) : Parcelable

@Parcelize
data class AnimeTrailer(
    @SerializedName("youtube_id") val youtubeId: String?,
    //@SerializedName("url") val youtubeUrl: String?,
    @SerializedName("embed_url") val embedUrl: String?
) : Parcelable

@Parcelize
data class AnimeJpg(
    val image_url: String? = null,
    val large_image_url: String? = null,
    val small_image_url: String? = null
) : Parcelable
