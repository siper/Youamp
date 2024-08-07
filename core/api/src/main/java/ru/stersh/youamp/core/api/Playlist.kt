package ru.stersh.youamp.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistResponse(
    @Json(name = "playlist")
    val playlist: Playlist
)

@JsonClass(generateAdapter = true)
data class PlaylistsResponse(
    @Json(name = "playlists")
    val playlists: PlaylistsData
) {
    @JsonClass(generateAdapter = true)
    data class PlaylistsData(
        @Json(name = "playlist")
        val playlist: List<Playlist>?
    )
}

@JsonClass(generateAdapter = true)
data class Playlist(
    @Json(name = "changed")
    val changed: String,
    @Json(name = "coverArt")
    val coverArt: String?,
    @Json(name = "created")
    val created: String,
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "entry")
    val entry: List<PlaylistEntry>?,
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "owner")
    val owner: String,
    @Json(name = "public")
    val `public`: Boolean?,
    @Json(name = "songCount")
    val songCount: Int,
)

@JsonClass(generateAdapter = true)
data class PlaylistEntry(
    @Json(name = "album")
    val album: String?,
    @Json(name = "albumId")
    val albumId: String?,
    @Json(name = "artist")
    val artist: String?,
    @Json(name = "artistId")
    val artistId: String?,
    @Json(name = "bitRate")
    val bitRate: Int?,
    @Json(name = "contentType")
    val contentType: String?,
    @Json(name = "coverArt")
    val coverArt: String?,
    @Json(name = "created")
    val created: String?,
    @Json(name = "discNumber")
    val discNumber: Int?,
    @Json(name = "duration")
    val duration: Int?,
    @Json(name = "genre")
    val genre: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "isDir")
    val isDir: Boolean,
    @Json(name = "isVideo")
    val isVideo: Boolean?,
    @Json(name = "parent")
    val parent: String?,
    @Json(name = "path")
    val path: String?,
    @Json(name = "playCount")
    val playCount: Int?,
    @Json(name = "size")
    val size: Int?,
    @Json(name = "suffix")
    val suffix: String?,
    @Json(name = "starred")
    val starred: String?,
    @Json(name = "userRating")
    val userRating: Int?,
    @Json(name = "title")
    val title: String,
    @Json(name = "track")
    val track: Int?,
    @Json(name = "transcodedContentType")
    val transcodedContentType: String?,
    @Json(name = "transcodedSuffix")
    val transcodedSuffix: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "year")
    val year: Int?,
)