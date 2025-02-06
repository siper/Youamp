package ru.stresh.youamp.feature.explore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PlayButton
import ru.stersh.youamp.core.ui.SectionTitle
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SongCardItem
import ru.stersh.youamp.core.ui.StateLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.explore.R
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stresh.youamp.feature.explore.ui.components.SearchBar

@Composable
fun ExploreScreen(
    onSearchClick: () -> Unit,
    onRandomSongsClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
) {
    val viewModel: ExploreViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ExploreScreen(
        state = state,
        onSearchClick = onSearchClick,
        onSongClick = onSongClick,
        onRandomSongsClick = onRandomSongsClick,
        onRetry = viewModel::retry,
        onPlayPauseAudioSource = viewModel::onPlayPauseAudioSource,
    )
}

@Composable
private fun ExploreScreen(
    state: StateUi,
    onSearchClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onRandomSongsClick: () -> Unit,
    onRetry: () -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    modifier: Modifier = Modifier
) {
    val layoutState by remember(state) {
        derivedStateOf {
            when {
                state.progress -> LayoutStateUi.Progress
                state.error -> LayoutStateUi.Error
                else -> LayoutStateUi.Content
            }
        }
    }
    StateLayout(
        state = layoutState,
        content = {
            Content(
                state = state,
                modifier = modifier,
                onSearchClick = onSearchClick,
                onSongClick = onSongClick,
                onRandomSongsClick = onRandomSongsClick,
                onPlayPauseAudioSource = onPlayPauseAudioSource
            )
        },
        progress = {
            Progress()
        },
        error = {
            ErrorLayout(onRetry = onRetry)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun Content(
    state: StateUi,
    onSearchClick: () -> Unit,
    onRandomSongsClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        item {
            SearchBar(
                onClick = onSearchClick,
                modifier = Modifier.padding(vertical = 98.dp, horizontal = 24.dp)
            )
        }
        state.data?.randomSongs?.let { randomSongs ->
            item {
                SectionTitle(
                    title = stringResource(R.string.random_songs_title),
                    onClick = onRandomSongsClick
                )
            }
            item {
                val lazyListState = rememberLazyListState()
                LazyRow(
                    state = lazyListState,
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    randomSongs.forEachIndexed { index, songChunk ->
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.requiredWidth(336.dp)
                            ) {
                                songChunk.forEach { item ->
                                    SongCardItem(
                                        title = item.title,
                                        artist = item.artist,
                                        artworkUrl = item.artworkUrl,
                                        onClick = {
                                            onSongClick(item.id)
                                        },
                                        playButton = {
                                            PlayButton(
                                                isPlaying = item.isPlaying,
                                                onClick = {
                                                    onPlayPauseAudioSource(AudioSource.Song(item.id))
                                                }
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun Progress(
    modifier: Modifier = Modifier
) {
    SkeletonLayout(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp
            )
        ) {
            SkeletonItem(
                shape = CircleShape,
                modifier = Modifier
                    .padding(vertical = 98.dp)
                    .requiredHeight(56.dp)
                    .fillMaxWidth()
            )
            SkeletonItem(
                modifier = Modifier.size(
                    200.dp,
                    48.dp
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
            }
        }
    }
}

@Immutable
internal data class StateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val data: DataUi? = null
)

@Immutable
internal data class DataUi(
    val randomSongs: List<List<SongUi>> = emptyList(),
)

@Composable
@Preview
private fun ExploreScreenPreview() {
    YouampPlayerTheme {
        ExploreScreen(
            state = StateUi(),
            onRetry = {},
            onSongClick = {},
            onRandomSongsClick = {},
            onPlayPauseAudioSource = {},
            onSearchClick = {}
        )
    }
}