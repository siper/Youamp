package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
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

@Immutable
data class PersonalAlbumUi(
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
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = {
            onClick(album.id)
        },
        modifier = modifier
    ) {
        ConstraintLayout {
            val (artwork, title, artist, playButton) = createRefs()

            Artwork(
                artworkUrl = album.artworkUrl,
                placeholder = Icons.Rounded.Album,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .constrainAs(artwork) {
                        top.linkTo(parent.top)
                    }
            )
            Text(
                text = album.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 4.dp,
                        bottom = 2.dp
                    )
                    .constrainAs(title) {
                        top.linkTo(playButton.bottom)
                    },
                textAlign = TextAlign.Center,
                minLines = 1,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge
            )
            album.artist?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                        .constrainAs(artist) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    textAlign = TextAlign.Center,
                    minLines = 1,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            OutlinedPlayButton(
                isPlaying = album.isPlaying,
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