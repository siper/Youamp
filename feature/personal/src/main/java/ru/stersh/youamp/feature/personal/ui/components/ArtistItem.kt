package ru.stersh.youamp.feature.personal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ru.stersh.youamp.core.ui.CircleArtwork

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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ArtistShape)
            .clickable {
                onClick(artist.id)
            },
    ) {
        ConstraintLayout {
            val (artwork, shadow, title, playButton) = createRefs()

            CircleArtwork(
                artworkUrl = artist.artworkUrl,
                placeholder = Icons.Rounded.Person,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .constrainAs(artwork) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.2f to Color.Transparent,
                                0.7f to MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ),
                        shape = CircleShape
                    )
                    .constrainAs(shadow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = artist.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 8.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
                    .constrainAs(title) {
                        top.linkTo(artwork.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedPlayButton(
                isPlaying = artist.isPlaying,
                onClick = onPlayPauseClick,
                modifier = Modifier.constrainAs(playButton) {
                    translationY = 44.dp
                    top.linkTo(artwork.top)
                    bottom.linkTo(artwork.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Stable
private val ArtistShape = RoundedCornerShape(36.dp)