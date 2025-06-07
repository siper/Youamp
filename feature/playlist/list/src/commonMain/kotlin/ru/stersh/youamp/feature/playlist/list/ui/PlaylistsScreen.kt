package ru.stersh.youamp.feature.playlist.list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.PlaylistItemDefaults
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.core.ui.isCompactWidth
import youamp.feature.playlist.list.generated.resources.Res
import youamp.feature.playlist.list.generated.resources.playlists_title


@Composable
fun PlaylistsScreen(
    onPlaylistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: PlaylistsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    PlaylistsScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onPlaylistClick = onPlaylistClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistsScreen(
    state: PlaylistsStateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onPlaylistClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                },
                title = {
                    Text(text = stringResource(Res.string.playlists_title))
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        PullToRefresh(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(it)
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
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState()
                        )
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = if (isCompactWidth) {
                            GridCells.Fixed(2)
                        } else {
                            GridCells.Adaptive(PlaylistItemDefaults.Width)
                        },
                        state = rememberLazyGridState(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = state.items
                        ) { playlist ->
                            PlaylistItem(
                                playlist = playlist,
                                onPlaylistClick = onPlaylistClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                key = { it },
                items = (0..10).toList()
            ) {
                SkeletonItem(
                    modifier = Modifier.height(220.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaylistItem(
    playlist: PlaylistUi,
    onPlaylistClick: (id: String) -> Unit
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.large,
        onClick = {
            onPlaylistClick(playlist.id)
        }
    ) {
        Artwork(
            artworkUrl = playlist.artworkUrl,
            placeholder = Icons.Rounded.MusicNote,
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        )
        Text(
            text = playlist.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Left,
            minLines = 1,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
@Preview
private fun PlaylistsScreenPreview() {
    YouampPlayerTheme {
        val items = persistentListOf(
            PlaylistUi(
                id = "1",
                name = "Test",
                artworkUrl = null
            ),
            PlaylistUi(
                id = "2",
                name = "Test 2",
                artworkUrl = null
            ),
            PlaylistUi(
                id = "3",
                name = "Test 3",
                artworkUrl = null
            )
        )
        val state = PlaylistsStateUi(
            progress = false,
            isRefreshing = true,
            error = false,
            items = items
        )
        PlaylistsScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onPlaylistClick = {},
            onBackClick = {}
        )
    }
}