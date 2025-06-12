package ru.stersh.youamp.feature.song.random.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import ru.stersh.youamp.core.ui.BackNavigationButton
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.HeaderLayout
import ru.stersh.youamp.core.ui.PlayAllButton
import ru.stersh.youamp.core.ui.PlayShuffledButton
import ru.stersh.youamp.core.ui.PullToRefresh
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SongItem
import ru.stersh.youamp.core.ui.SongSkeleton
import youamp.feature.song.random.generated.resources.Res
import youamp.feature.song.random.generated.resources.random_songs_title

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

@OptIn(ExperimentalMaterial3Api::class)
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
                    Text(
                        text = stringResource(Res.string.random_songs_title),
                        modifier = Modifier.singleHeader()
                    )
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
            SongItem(
                title = song.title,
                artist = song.artist,
                artworkUrl = song.artworkUrl,
                onClick = { onSongClick(song.id) }
            )
        }
    }
}

@Composable
private fun Progress() {
    SkeletonLayout(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                HeaderLayout(
                    title = {
                        Column(
                            modifier = Modifier.singleHeader(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SkeletonItem(
                                modifier = Modifier
                                    .size(
                                        400.dp,
                                        32.dp
                                    )
                            )
                            SkeletonItem(
                                modifier = Modifier
                                    .size(
                                        200.dp,
                                        32.dp
                                    )
                            )
                        }
                    },
                    actions = {
                        SkeletonItem(
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp)
                        )
                        SkeletonItem(
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
            repeat(10) {
                item { SongSkeleton() }
            }
        }
    }
}

@Preview
@Composable
private fun RandomSongsScreenProgressPreview() {
    MaterialTheme {
        RandomSongsScreen(
            state = StateUi(),
            onPlayAll = {},
            onPlayShuffled = {},
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onBackClick = {}
        )
    }
}

@Preview
@Composable
private fun RandomSongsScreenPreview() {
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