package ru.stersh.youamp.feature.explore.ui

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.Section
import ru.stersh.youamp.core.ui.SectionScrollActions
import ru.stersh.youamp.core.ui.SectionSkeleton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SongCardChunkSkeleton
import ru.stersh.youamp.core.ui.SongCardDefaults
import ru.stersh.youamp.core.ui.SongCardItem
import ru.stersh.youamp.core.ui.SongCardType
import ru.stersh.youamp.core.ui.StateLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.currentPlatform
import ru.stersh.youamp.feature.explore.ui.components.SearchBar
import ru.stersh.youamp.shared.queue.AudioSource
import youamp.feature.explore.generated.resources.Res
import youamp.feature.explore.generated.resources.random_songs_title

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
        onRefresh = viewModel::refresh,
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
    onRefresh: () -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    modifier: Modifier = Modifier,
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
    PullToRefresh(
        isRefreshing = state.refreshing,
        onRefresh = onRefresh,
    ) {
        StateLayout(
            state = layoutState,
            content = {
                Content(
                    state = state,
                    modifier = modifier,
                    onSearchClick = onSearchClick,
                    onSongClick = onSongClick,
                    onRandomSongsClick = onRandomSongsClick,
                    onPlayPauseAudioSource = onPlayPauseAudioSource,
                )
            },
            progress = {
                Progress()
            },
            error = {
                ErrorLayout(onRetry = onRetry)
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
        )
    }
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
        modifier =
            modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
    ) {
        item {
            SearchBar(
                onClick = onSearchClick,
                modifier =
                    Modifier.padding(
                        vertical = 98.dp,
                        horizontal = 24.dp,
                    ),
            )
        }
        state.data?.randomSongs?.let { randomSongs ->
            item {
                val lazyListState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.random_songs_title),
                    onClick = onRandomSongsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(lazyListState)
                        }
                    },
                ) {
                    LazyRow(
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        randomSongs.forEachIndexed { index, songChunk ->
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.requiredWidth(SongCardDefaults.Width),
                                ) {
                                    songChunk.forEachIndexed { index, item ->
                                        SongCardItem(
                                            title = item.title,
                                            artist = item.artist,
                                            artworkUrl = item.artworkUrl,
                                            onClick = {
                                                onSongClick(item.id)
                                            },
                                            type =
                                                when (index) {
                                                    0 -> SongCardType.Top
                                                    1 -> SongCardType.Center
                                                    2 -> SongCardType.Bottom
                                                    else -> SongCardType.Default
                                                },
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
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
private fun Progress(modifier: Modifier = Modifier) {
    SkeletonLayout(modifier = modifier) {
        LazyColumn(
            modifier =
                Modifier.padding(
                    horizontal = 24.dp,
                ),
            userScrollEnabled = false,
        ) {
            item {
                SkeletonItem(
                    shape = CircleShape,
                    modifier =
                        Modifier
                            .padding(vertical = 98.dp)
                            .requiredHeight(56.dp)
                            .fillMaxWidth(),
                )
            }
            item { SectionSkeleton() }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false,
                ) {
                    repeat(2) {
                        item { SongCardChunkSkeleton() }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ExploreScreenProgressPreview() {
    YouampPlayerTheme {
        ExploreScreen(
            state = StateUi(),
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onRandomSongsClick = {},
            onPlayPauseAudioSource = {},
            onSearchClick = {},
        )
    }
}

@Composable
@Preview
private fun ExploreScreenPreview() {
    YouampPlayerTheme {
        ExploreScreen(
            state =
                StateUi(
                    progress = false,
                    error = false,
                    data =
                        DataUi(
                            randomSongs =
                                persistentListOf(
                                    persistentListOf(
                                        SongUi(
                                            id = "Julieta",
                                            title = "Tykia",
                                            artist = null,
                                            artworkUrl = null,
                                            isPlaying = false,
                                        ),
                                        SongUi(
                                            id = "Kofi",
                                            title = "Lyla",
                                            artist = null,
                                            artworkUrl = null,
                                            isPlaying = false,
                                        ),
                                        SongUi(
                                            id = "Kofi",
                                            title = "Lyla",
                                            artist = "Kofi",
                                            artworkUrl = null,
                                            isPlaying = false,
                                        ),
                                    ),
                                ),
                        ),
                ),
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onRandomSongsClick = {},
            onPlayPauseAudioSource = {},
            onSearchClick = {},
        )
    }
}
