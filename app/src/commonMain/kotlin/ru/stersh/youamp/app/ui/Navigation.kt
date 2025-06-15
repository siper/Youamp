package ru.stersh.youamp.app.ui

import kotlinx.serialization.Serializable

@Serializable
object Main

@Serializable
data class AlbumInfo(
    val albumId: String,
)

@Serializable
object Player

@Serializable
data class ArtistInfo(
    val artistId: String,
)

@Serializable
object PlayQueue

@Serializable
object Search

@Serializable
object AddServer

@Serializable
object ServerList

@Serializable
data class ServerEditor(
    val serverId: Long,
)

@Serializable
data class PlaylistInfo(
    val playlistId: String,
)

@Serializable
data class SongInfo(
    val songId: String,
    val showAlbum: Boolean,
)

@Serializable
object Settings

@Serializable
object About

@Serializable
object Albums

@Serializable
object Artists

@Serializable
object Playlists

@Serializable
object FavoriteSongs

@Serializable
object RandomSongs

@Serializable
object FavoriteAlbums

@Serializable
object FavoriteArtists
