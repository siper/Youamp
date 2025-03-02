package ru.stersh.youamp.feature.personal.domain

internal data class Personal(
    val playlists: List<Playlist>,
    val songs: List<Song>,
    val albums: List<Album>,
    val artists: List<Artist>
)