package ru.stersh.youamp.feature.player.queue.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val isCurrent: Boolean,
    val isPlaying: Boolean,
    val artworkUrl: String?
)
