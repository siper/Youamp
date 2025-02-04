package ru.stresh.youamp.feature.song.random.ui

import androidx.compose.runtime.Immutable

internal data class StateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null
)

@Immutable
internal data class DataUi(
    val songs: List<SongUi>
)

internal class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)