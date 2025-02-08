package ru.stresh.youamp.feature.song.random.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.HeaderTitle
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.feature.song.random.R

@Composable
fun RandomSongsScreen(
    onSongClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: RandomSongsViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    RandomSongsScreen(
        state = state,
        onPlayAll = viewModel::playAll,
        onPlayShuffled = viewModel::playShuffled,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onSongClick = onSongClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun RandomSongsScreen(
    state: StateUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackNavigationButton(onClick = onBackClick)
                }
            )
        }
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
                    Content(
                        data = state.data,
                        onPlayAll = onPlayAll,
                        onPlayShuffled = onPlayShuffled,
                        onSongClick = onSongClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    data: DataUi,
    onPlayAll: () -> Unit,
    onPlayShuffled: () -> Unit,
    onSongClick: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item(
            contentType = "header",
            key = "header"
        ) {
            HeaderLayout(
                title = {
                    HeaderTitle(text = stringResource(R.string.random_songs_title))
                },
                actions = {
                    PlayAllButton(
                        onClick = onPlayAll
                    )
                    PlayShuffledButton(
                        onClick = onPlayShuffled
                    )
                }
            )
        }
        items(
            items = data.songs,
            contentType = { "song" },
            key = { "song_${it.id}" }
        ) { song ->
            RandomSongItem(
                song = song,
                onClick = { onSongClick(song.id) }
            )
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
private fun RandomSongItem(
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
        val songs = persistentListOf(
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
        RandomSongsScreen(
            state = state,
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onBackClick = {}
        )
    }
}