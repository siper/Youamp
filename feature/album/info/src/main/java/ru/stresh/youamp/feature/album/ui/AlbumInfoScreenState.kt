package ru.stresh.youamp.feature.album.ui

internal sealed interface AlbumInfoScreenState {
    data class Content(
        val coverArtUrl: String?,
        val title: String,
        val artist: String,
        val year: String?,
        val songs: List<AlbumSongUi>
    ) : AlbumInfoScreenState

    object Error : AlbumInfoScreenState
    object Progress : AlbumInfoScreenState
}