package ru.stersh.youamp.shared.player.metadata

data class SongInfo(
    val id: String,
    val title: String?,
    val artist: String?,
    val album: String?,
    val coverArtUrl: String?,
    val favorite: Boolean,
    val rating: Int,
)
