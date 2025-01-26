package ru.stersh.youamp.feature.player.queue.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class StateUi(
    val songs: List<SongUi> = emptyList(),
    val progress: Boolean = true,
    val menuSongState: MenuSongStateUi? = null
)

@Immutable
internal data class MenuSongStateUi(
    val title: String?,
    val artist: String?,
    val artworkUrl: String?,
    val index: Int
)