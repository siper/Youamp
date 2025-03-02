package ru.stersh.youamp.feature.player.queue.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class StateUi(
    val songs: ImmutableList<SongUi> = persistentListOf(),
    val progress: Boolean = true,
    val menuSongState: MenuSongStateUi? = null
)

internal data class MenuSongStateUi(
    val title: String?,
    val artist: String?,
    val artworkUrl: String?,
    val index: Int
)

internal data class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val isCurrent: Boolean,
    val isPlaying: Boolean,
    val artworkUrl: String?
)
