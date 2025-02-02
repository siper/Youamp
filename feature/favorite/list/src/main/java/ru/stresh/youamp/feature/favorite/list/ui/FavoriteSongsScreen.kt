package ru.stresh.youamp.feature.favorite.list.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.PlayAllFabButton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.feature.favorite.list.R

@Composable
fun FavoriteSongsScreen(
    onSongClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: FavoriteSongsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    FavoriteSongsScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onSongClick = onSongClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun FavoriteSongsScreen(
    state: StateUi,
    onPlayAll: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSongClick: (id: String) -> Unit,
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
                    Text(text = stringResource(R.string.favorite_songs_title))
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        PullToRefreshBox(
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

                state.data != null && state.data.songs.isEmpty() -> {
                    EmptyLayout(
                        modifier = Modifier.verticalScroll(
                            state = rememberScrollState()
                        )
                    )
                }

                state.data?.songs != null -> {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                items = state.data.songs,
                                contentType = { "song" },
                                key = { "song_${it.id}" }
                            ) { song ->
                                FavoriteSongItem(
                                    song = song,
                                    onClick = { onSongClick(song.id) }
                                )
                            }
                            item(key = "fab_spacer") {
                                Spacer(modifier = Modifier.height(88.dp))
                            }
                        }
                        PlayAllFabButton(
                            onClick = onPlayAll,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout {
        repeat(10) {
            ListItem(
                headlineContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeletonItem(modifier = Modifier.size(width = 130.dp, height = 16.dp))
                        SkeletonItem(modifier = Modifier.size(width = 200.dp, height = 16.dp))
                    }
                },
                leadingContent = {
                    SkeletonItem(modifier = Modifier.size(48.dp))
                }
            )
        }
    }
}

@Composable
private fun FavoriteSongItem(
    song: SongUi,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            song.artist?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        leadingContent = {
            Artwork(
                artworkUrl = song.artworkUrl,
                placeholder = Icons.Rounded.MusicNote,
                modifier = Modifier.size(48.dp)
            )
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteSongsScreenPreview() {
    MaterialTheme {
        val songs = listOf(
            SongUi(
                id = "1",
                title = "Best song in the world 1",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            SongUi(
                id = "2",
                title = "Best song in the world 2",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            SongUi(
                id = "3",
                title = "Best song in the world 3",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            SongUi(
                id = "4",
                title = "Best song in the world 4",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            SongUi(
                id = "5",
                title = "Best song in the world 5",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
        )
        val state = StateUi(
            progress = false,
            isRefreshing = false,
            error = false,
            data = DataUi(
                songs = songs
            )
        )
        FavoriteSongsScreen(
            state = state,
            onPlayAll = {},
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onBackClick = {}
        )
    }
}