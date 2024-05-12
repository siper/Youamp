package ru.stresh.youamp.feature.player.mini.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.SingleLineText

@Composable
fun MiniPlayer(
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: () -> Unit
) {
    val viewModel: MiniPlayerViewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)
    val state by viewModel.state.collectAsStateWithLifecycle()

    MiniPlayer(
        state = state,
        onClick = onClick,
        onPlayPauseClick = viewModel::playPause
    )
}

@Composable
@Preview
private fun MiniPlayerPreview() {
    val state = StateUi.Content(
        title = "Test title",
        artist = "Test artist",
        artworkUrl = "",
        isPlaying = true,
        progress = 0.5f
    )
    MiniPlayer(
        state = state,
        onClick = {},
        onPlayPauseClick = {}
    )
}

@Composable
private fun MiniPlayer(
    state: StateUi,
    onPlayPauseClick: () -> Unit,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        enter = expandVertically(
            animationSpec = tween(
                durationMillis = 100
            )
        ),
        exit = shrinkVertically(
            animationSpec = tween(
                durationMillis = 100
            )
        ),
        visible = state is StateUi.Content
    ) {
        if (state !is StateUi.Content) {
            return@AnimatedVisibility
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick.invoke() },
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Artwork(
                    artworkUrl = state.artworkUrl,
                    placeholder = Icons.Rounded.Album,
                    modifier = Modifier
                        .size(88.dp)
                        .padding(12.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    SingleLineText(
                        text = state.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    SingleLineText(
                        text = state.artist,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { state.progress },
                        strokeCap = StrokeCap.Round
                    )
                    PlayPauseButton(
                        modifier = Modifier.size(72.dp),
                        isPlaying = state.isPlaying,
                        onIsPlayedChanged = { onPlayPauseClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipPrevious,
            contentDescription = "Previous song",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Next song"
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: State<Boolean>,
    onIsFavoriteChanged: (isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isFavorite.value,
        onCheckedChange = onIsFavoriteChanged,
        modifier = modifier
    ) {
        if (isFavorite.value) {
            Icon(Icons.Rounded.Favorite, contentDescription = "Dislike button")
        } else {
            Icon(Icons.Rounded.FavoriteBorder, contentDescription = "Like button")
        }
    }
}

@Composable
private fun PlayPauseButton(
    isPlaying: Boolean,
    onIsPlayedChanged: (isPlayed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isPlaying,
        onCheckedChange = onIsPlayedChanged,
        modifier = modifier
    ) {
        if (isPlaying) {
            Icon(
                imageVector = Icons.Rounded.Pause,
                contentDescription = "Pause button"
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play button"
            )
        }
    }
}