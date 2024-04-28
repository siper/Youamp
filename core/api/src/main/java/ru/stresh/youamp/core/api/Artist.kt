package ru.stresh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistsResponse(
    @Json(name = "artists") val artists: Artists
)

@JsonClass(generateAdapter = true)
data class Artists(
    @Json(name = "ignoredArticles") val ignoredArticles: String,
    @Json(name = "index") val indices: List<Index>,
) {

    @JsonClass(generateAdapter = true)
    data class Index(
        @Json(name = "name") val name: String,
        @Json(name = "artist") val artists: List<Artist>,
    )
}

@JsonClass(generateAdapter = true)
data class ArtistResponse(
    @Json(name = "artist")
    val artist: Artist
)

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "coverArt")
    val coverArt: String,
    @Json(name = "albumCount")
    val albumCount: Int,
    @Json(name = "artistImageUrl")
    val artistImageUrl: String?,
    @Json(name = "album")
    val albums: List<Album>?,
)
