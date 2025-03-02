package ru.stresh.youamp.feature.explore.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class StateUi(
    val progress: Boolean = true,
    val refreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null
)

internal data class DataUi(
    val randomSongs: ImmutableList<ImmutableList<SongUi>> = persistentListOf(),
)

internal data class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)