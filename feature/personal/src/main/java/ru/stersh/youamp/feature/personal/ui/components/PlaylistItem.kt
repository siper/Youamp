package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.PlayButtonOutlined

@Immutable
internal data class PlaylistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

@Composable
internal fun PlaylistItem(
    playlist: PlaylistUi,
    onPlayPauseClick: () -> Unit,
    onClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = {
            onClick(playlist.id)
        },
        modifier = modifier
    ) {
        ConstraintLayout {
            val (artwork, title, playButton) = createRefs()

            Artwork(
                artworkUrl = playlist.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .constrainAs(artwork) {
                        top.linkTo(parent.top)
                    }
            )
            Text(
                text = playlist.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 4.dp,
                        bottom = 12.dp
                    )
                    .constrainAs(title) {
                        top.linkTo(playButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                textAlign = TextAlign.Center,
                minLines = 1,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge
            )
            PlayButtonOutlined(
                isPlaying = playlist.isPlaying,
                onClick = onPlayPauseClick,
                modifier = Modifier.constrainAs(playButton) {
                    top.linkTo(artwork.bottom)
                    bottom.linkTo(artwork.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}