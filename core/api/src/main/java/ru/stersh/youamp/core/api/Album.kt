package ru.stersh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumResponse(
    @Json(name = "album")
    val album: Album
)

@JsonClass(generateAdapter = true)
data class AlbumListResponse(
    @Json(name = "albumList")
    val albumList: AlbumList
)

@JsonClass(generateAdapter = true)
data class AlbumList2Response(
    @Json(name = "albumList2")
    val albumList: AlbumList
)

@JsonClass(generateAdapter = true)
data class AlbumList(
    @Json(name = "album")
    val albums: List<Album>?
)

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "artist")
    val artist: String,
    @Json(name = "artistId")
    val artistId: String?,
    @Json(name = "coverArt")
    val coverArt: String?,
    @Json(name = "created")
    val created: String,
    @Json(name = "duration")
    val duration: Int?,
    @Json(name = "genre")
    val genre: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "album")
    val album: String?,
    @Json(name = "playCount")
    val playCount: Int?,
    @Json(name = "song")
    val song: List<Song>?,
    @Json(name = "songCount")
    val songCount: Int?,
    @Json(name = "year")
    val year: Int?,
    @Json(name = "userRating")
    val userRating: Int?
)

@JsonClass(generateAdapter = true)
data class SearchResult3Response(
    @Json(name = "searchResult3")
    val searchResult3: SearchResult3
)

@JsonClass(generateAdapter = true)
data class SearchResult3(
    @Json(name = "song")
    val song: List<Song>?,
    @Json(name = "album")
    val album: List<Album>?,
    @Json(name = "artist")
    val artist: List<Artist>?
)