package ru.stresh.youamp.feature.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PlayButtonOutlined
import ru.stersh.youamp.core.ui.SectionTitle
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.StateLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stresh.youamp.shared.queue.AudioSource
import youamp.feature.library.generated.resources.Res
import youamp.feature.library.generated.resources.albums_title
import youamp.feature.library.generated.resources.artists_title

@Composable
fun LibraryScreen(
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
) {
    val viewModel: LibraryViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LibraryScreen(
        state = state,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onAlbumsClick = onAlbumsClick,
        onArtistsClick = onArtistsClick,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onPlayPauseAudioSource = viewModel::onPlayPauseAudioSource
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryScreen(
    state: StateUi,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
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
    PullToRefreshBox(
        isRefreshing = state.refreshing,
        onRefresh = onRefresh
    ) {
        StateLayout(
            state = layoutState,
            content = {
                Content(
                    data = state.data,
                    onAlbumClick = onAlbumClick,
                    onArtistClick = onArtistClick,
                    onAlbumsClick = onAlbumsClick,
                    onArtistsClick = onArtistsClick,
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
}

@Composable
private fun Content(
    data: DataUi?,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    modifier: Modifier = Modifier
) {
    if (data == null) {
        return
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (data.albums.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(Res.string.albums_title),
                    onClick = onAlbumsClick
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
                    items(data.albums) {
                        AlbumItem(
                            title = it.title,
                            artist = it.artist,
                            artworkUrl = it.artworkUrl,
                            playButton = {
                                PlayButtonOutlined(
                                    isPlaying = it.isPlaying,
                                    onClick = {
                                        onPlayPauseAudioSource(AudioSource.Album(it.id))
                                    }
                                )
                            },
                            onClick = {
                                onAlbumClick(it.id)
                            },
                            modifier = Modifier.requiredWidth(160.dp)
                        )
                    }
                }
            }
        }
        if (data.artists.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(Res.string.artists_title),
                    onClick = onArtistsClick
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
                    items(data.artists) {
                        ArtistItem(
                            name = it.name,
                            artworkUrl = it.artworkUrl,
                            playButton = {
                                PlayButtonOutlined(
                                    isPlaying = it.isPlaying,
                                    onClick = {
                                        onPlayPauseAudioSource(AudioSource.Artist(it.id))
                                    }
                                )
                            },
                            onClick = {
                                onArtistClick(it.id)
                            },
                            modifier = Modifier.requiredWidth(160.dp)
                        )
                    }
                }
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
                modifier = Modifier.size(
                    200.dp,
                    48.dp
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier.size(
                        160.dp,
                        200.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        160.dp,
                        200.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        160.dp,
                        200.dp
                    )
                )
            }
            SkeletonItem(
                modifier = Modifier.size(
                    200.dp,
                    48.dp
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier
                        .size(
                            160.dp,
                            160.dp
                        )
                        .clip(CircleShape)
                )
                SkeletonItem(
                    modifier = Modifier
                        .size(
                            160.dp,
                            160.dp
                        )
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
@Preview
private fun LibraryScreenPreview() {
    YouampPlayerTheme {
        LibraryScreen(
            state = StateUi(),
            onRetry = {},
            onRefresh = {},
            onAlbumClick = {},
            onArtistClick = {},
            onArtistsClick = {},
            onAlbumsClick = {},
            onPlayPauseAudioSource = {}
        )
    }
}