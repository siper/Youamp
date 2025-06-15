package ru.stersh.youamp.feature.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.AlbumItemDefaults
import ru.stersh.youamp.core.ui.AlbumSkeleton
import ru.stersh.youamp.core.ui.ArtistItem
import ru.stersh.youamp.core.ui.ArtistSkeleton
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.Section
import ru.stersh.youamp.core.ui.SectionScrollActions
import ru.stersh.youamp.core.ui.SectionSkeleton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.StateLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.currentPlatform
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
    )
}

@Composable
internal fun LibraryScreen(
    state: StateUi,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
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
    PullToRefresh(
        isRefreshing = state.refreshing,
        onRefresh = onRefresh,
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
    data: DataUi?,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onAlbumsClick: () -> Unit,
    onArtistsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (data == null) {
        return
    }
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        if (data.albums.isNotEmpty()) {
            item {
                val albumsListState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.albums_title),
                    onClick = onAlbumsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(albumsListState)
                        }
                    },
                ) {
                    LazyRow(
                        state = albumsListState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = albumsListState),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(data.albums) {
                            AlbumItem(
                                title = it.title,
                                artist = it.artist,
                                artworkUrl = it.artworkUrl,
                                onClick = {
                                    onAlbumClick(it.id)
                                },
                                modifier = Modifier.requiredWidth(AlbumItemDefaults.Width),
                            )
                        }
                    }
                }
            }
        }
        if (data.artists.isNotEmpty()) {
            item {
                val lazyListState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.artists_title),
                    onClick = onArtistsClick,
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
                        items(data.artists) {
                            ArtistItem(
                                name = it.name,
                                artworkUrl = it.artworkUrl,
                                onClick = {
                                    onArtistClick(it.id)
                                },
                                modifier = Modifier.requiredWidth(160.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress(modifier: Modifier = Modifier) {
    SkeletonLayout(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            userScrollEnabled = false,
        ) {
            item { SectionSkeleton() }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false,
                ) {
                    repeat(3) {
                        item { AlbumSkeleton() }
                    }
                }
            }
            item { SectionSkeleton() }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false,
                ) {
                    repeat(3) {
                        item { ArtistSkeleton() }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun LibraryScreenProgressPreview() {
    YouampPlayerTheme {
        LibraryScreen(
            state = StateUi(),
            onRetry = {},
            onRefresh = {},
            onAlbumClick = {},
            onArtistClick = {},
            onArtistsClick = {},
            onAlbumsClick = {},
        )
    }
}

@Composable
@Preview
private fun LibraryScreenPreview() {
    YouampPlayerTheme {
        LibraryScreen(
            state =
                StateUi(
                    progress = false,
                    data =
                        DataUi(
                            albums =
                                persistentListOf(
                                    AlbumUi(
                                        id = "Kashif",
                                        title = "Tremaine",
                                        artist = "Slipknot",
                                        artworkUrl = null,
                                        isPlaying = false,
                                    ),
                                ),
                            artists =
                                persistentListOf(
                                    ArtistUi(
                                        id = "Soloman",
                                        name = "Eliana",
                                        artworkUrl = null,
                                        isPlaying = false,
                                    ),
                                    ArtistUi(
                                        id = "Tari",
                                        name = "Shamir",
                                        artworkUrl = null,
                                        isPlaying = false,
                                    ),
                                ),
                        ),
                ),
            onRetry = {},
            onRefresh = {},
            onAlbumClick = {},
            onArtistClick = {},
            onArtistsClick = {},
            onAlbumsClick = {},
        )
    }
}
