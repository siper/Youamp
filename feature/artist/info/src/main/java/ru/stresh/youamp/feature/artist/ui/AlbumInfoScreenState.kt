package ru.stresh.youamp.feature.artist.ui

import androidx.compose.runtime.Immutable
import ru.stresh.youamp.core.ui.AlbumUi

@Immutable
internal data class AlbumInfoStateUi(
    val coverArtUrl: String? = null,
    val name: String? = null,
    val progress: Boolean = true,
    val albums: List<AlbumUi> = emptyList()
)