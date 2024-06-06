package ru.stersh.youamp.feature.artist.ui

import androidx.compose.runtime.Immutable
import ru.stersh.youamp.core.ui.AlbumUi

@Immutable
internal data class ArtistInfoStateUi(
    val progress: Boolean = true,
    val error: Boolean = true,
    val content: ArtistInfoUi? = null
)

@Immutable
internal data class ArtistInfoUi(
    val artworkUrl: String? = null,
    val name: String,
    val albums: List<AlbumUi>
)