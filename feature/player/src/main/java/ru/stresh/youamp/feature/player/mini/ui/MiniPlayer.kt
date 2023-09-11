package ru.stresh.youamp.feature.player.mini.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import ru.stresh.youamp.core.ui.Artwork
import ru.stresh.youamp.core.ui.SingleLineText

@Composable
fun MiniPlayer(
    onClick: () -> Unit
) {
    MiniPlayer(
        onClick = onClick,
        viewModel = koinViewModel()
    )
}

@Composable
@Preview
private fun MiniPlayerPreview() {
    MiniPlayer(
        onClick = {},
        viewModel = viewModel()
    )
}

@Composable
private fun MiniPlayer(
    onClick: () -> Unit,
    viewModel: MiniPlayerViewModel
) {
    val artworkUrl = viewModel.artworkUrl.collectAsStateWithLifecycle(initialValue = null)
    val title = viewModel.title.collectAsStateWithLifecycle(initialValue = null)
    val artist = viewModel.artist.collectAsStateWithLifecycle(initialValue = null)
    val isPlaying = viewModel.isPlaying.collectAsStateWithLifecycle(initialValue = false)
    val isNeedShowPlayer =
        viewModel.isNeedShowPlayer.collectAsStateWithLifecycle(initialValue = false)
    val progress = viewModel.progress.collectAsStateWithLifecycle(initialValue = 0f)
    if (!isNeedShowPlayer.value) {
        return
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .clickable { onClick.invoke() },
        color = MaterialTheme.colorScheme.secondaryContainer,
        shadowElevation = 3.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Artwork(
                artworkUrl = artworkUrl.value,
                modifier = Modifier
                    .size(56.dp)
                    .padding(8.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.8f)
            ) {
                SingleLineText(
                    text = title.value,
                    style = MaterialTheme.typography.titleMedium
                )
                SingleLineText(
                    text = artist.value,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress.value },
                    strokeCap = StrokeCap.Round
                )
                PlayPauseButton(
                    isPlaying = isPlaying,
                    onIsPlayedChanged = { viewModel.playPause() }
                )
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
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.SkipNext,
            contentDescription = "Next song",
            tint = MaterialTheme.colorScheme.secondary
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
    isPlaying: State<Boolean>,
    onIsPlayedChanged: (isPlayed: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isPlaying.value,
        onCheckedChange = onIsPlayedChanged,
        modifier = modifier
    ) {
        if (isPlaying.value) {
            Icon(Icons.Rounded.Pause, contentDescription = "Pause button")
        } else {
            Icon(Icons.Rounded.PlayArrow, contentDescription = "Play button")
        }
    }
}