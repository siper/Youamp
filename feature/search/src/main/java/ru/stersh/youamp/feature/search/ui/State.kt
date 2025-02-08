package ru.stersh.youamp.feature.search.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class SearchStateUi(
    val progress: Boolean = true,
    val songs: ImmutableList<Song> = persistentListOf(),
    val songsProgress: Boolean = false,
    val hasMoreSongs: Boolean = true,
    val albums: ImmutableList<Album> = persistentListOf(),
    val albumsProgress: Boolean = false,
    val hasMoreAlbums: Boolean = true,
    val artists: ImmutableList<Artist> = persistentListOf(),
    val artistsProgress: Boolean = false,
    val hasMoreArtists: Boolean = true,
)

internal data class Song(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)

internal data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String?
)

internal data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String?
)