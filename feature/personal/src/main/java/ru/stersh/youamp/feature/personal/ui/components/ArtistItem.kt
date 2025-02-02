package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.PlayButtonOutlined

@Immutable
internal data class PersonalArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

@Composable
internal fun ArtistItem(
    artist: PersonalArtistUi,
    onPlayPauseClick: () -> Unit,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ArtistItem(
        name = artist.name,
        artworkUrl = artist.artworkUrl,
        playButton = {
            PlayButtonOutlined(
                isPlaying = artist.isPlaying,
                onClick = onPlayPauseClick
            )
        },
        onClick = { onClick(artist.id) },
        modifier = modifier
    )
}