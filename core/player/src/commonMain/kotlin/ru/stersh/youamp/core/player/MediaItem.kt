package ru.stersh.youamp.core.player

data class MediaItem(
    val id: String,
    val title: String,
    val url: String,
    val artist: String? = null,
    val album: String? = null,
    val artworkUrl: String? = null,
)
