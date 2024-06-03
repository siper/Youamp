package ru.stersh.youamp.main.ui

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


@Serializable
object Main

@Serializable
data class AlbumInfo(val albumId: String)

@Serializable
object Player

@Serializable
data class ArtistInfo(val artistId: String)

@Serializable
object PlayQueue

@Serializable
object Search

@Serializable
object AddServer

@Serializable
object ServerList

@Serializable
data class ServerEditor(val serverId: Long)

@Serializable
data class PlaylistInfo(val playlistId: String)

@Immutable
data class SongInfoProperties(
    val songId: String,
    val showAlbum: Boolean
)