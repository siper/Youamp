package ru.stersh.youamp.feature.album.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.AlbumItem
import ru.stersh.youamp.core.ui.AlbumItemDefaults
import ru.stersh.youamp.core.ui.AlbumSkeleton
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.OnBottomReached
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.isCompactWidth
import youamp.feature.album.list.generated.resources.Res
import youamp.feature.album.list.generated.resources.albums_title

@Composable
fun AlbumsScreen(
    onBackClick: () -> Unit,
    onAlbumClick: (id: String) -> Unit,
) {
    val viewModel: AlbumsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    AlbumsScreen(
        state = state,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::retry,
        onBottomReached = viewModel::loadMore,
        onAlbumClick = onAlbumClick,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumsScreen(
    state: AlbumsStateUi,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onBottomReached: () -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(Res.string.albums_title))
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it),
        ) {
            when {
                state.progress -> {
                    Progress()
                }

                state.error -> {
                    ErrorLayout(onRetry = onRetry)
                }

                state.items.isEmpty() -> {
                    EmptyLayout(
                        modifier =
                            Modifier.verticalScroll(
                                state = rememberScrollState(),
                            ),
                    )
                }

                else -> {
                    val listState = rememberLazyGridState()
                    Content(
                        listState = listState,
                        state = state,
                        onAlbumClick = onAlbumClick,
                    )

                    listState.OnBottomReached {
                        onBottomReached()
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    listState: LazyGridState,
    state: AlbumsStateUi,
    onAlbumClick: (id: String) -> Unit,
) {
    LazyVerticalGrid(
        columns =
            if (isCompactWidth) {
                GridCells.Fixed(2)
            } else {
                GridCells.Adaptive(AlbumItemDefaults.Width)
            },
        state = listState,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            key = { it.id },
            items = state.items,
        ) { album ->
            AlbumItem(
                title = album.title,
                artist = album.artist,
                artworkUrl = album.artworkUrl,
                onClick = { onAlbumClick(album.id) },
            )
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        LazyVerticalGrid(
            columns =
                if (isCompactWidth) {
                    GridCells.Fixed(2)
                } else {
                    GridCells.Adaptive(AlbumItemDefaults.Width)
                },
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                key = { it },
                items = (0..10).toList(),
            ) {
                AlbumSkeleton()
            }
        }
    }
}

@Composable
@Preview
private fun AlbumsScreenProgressPreview() {
    YouampPlayerTheme {
        AlbumsScreen(
            state = AlbumsStateUi(),
            onRefresh = {},
            onRetry = {},
            onBottomReached = {},
            onAlbumClick = {},
            onBackClick = {},
        )
    }
}

@Composable
@Preview
private fun AlbumsScreenPreview() {
    YouampPlayerTheme {
        val items =
            persistentListOf(
                AlbumUi(
                    id = "1",
                    title = "Test",
                    artist = "Test artist",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "2",
                    title = "Test 2",
                    artist = "Test artist 2 ",
                    artworkUrl = null,
                ),
                AlbumUi(
                    id = "3",
                    title = "Test 3",
                    artist = "Test artist 3",
                    artworkUrl = null,
                ),
            )
        val state =
            AlbumsStateUi(
                progress = false,
                isRefreshing = false,
                error = false,
                items = items,
            )
        AlbumsScreen(
            state = state,
            onRefresh = {},
            onRetry = {},
            onBottomReached = {},
            onAlbumClick = {},
            onBackClick = {},
        )
    }
}
