package ru.stresh.youamp.feature.search.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class SearchStateUi(
    val progress: Boolean = true,
    val songs: List<SearchResultUi.Song> = emptyList(),
    val songsProgress: Boolean = false,
    val hasMoreSongs: Boolean = true,
    val albums: List<SearchResultUi.Album> = emptyList(),
    val albumsProgress: Boolean = false,
    val hasMoreAlbums: Boolean = true,
    val artists: List<SearchResultUi.Artist> = emptyList(),
    val artistsProgress: Boolean = false,
    val hasMoreArtists: Boolean = true,
)