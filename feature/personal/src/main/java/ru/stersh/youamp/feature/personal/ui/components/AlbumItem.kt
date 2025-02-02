package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.PlayButtonOutlined

@Immutable
internal data class PersonalAlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

@Composable
internal fun AlbumPersonalItem(
    album: PersonalAlbumUi,
    onPlayPauseClick: () -> Unit,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    AlbumItem(
        title = album.title,
        onClick = { onClick(album.id) },
        artist = album.artist,
        artworkUrl = album.artworkUrl,
        playButton = {
            PlayButtonOutlined(
                isPlaying = album.isPlaying,
                onClick = onPlayPauseClick
            )
        },
        modifier = modifier
    )
}