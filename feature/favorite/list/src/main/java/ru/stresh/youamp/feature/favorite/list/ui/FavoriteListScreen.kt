package ru.stresh.youamp.feature.favorite.list.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.Artwork
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.SkeletonScope

@Composable
fun FavoriteListScreen(
    viewModelStoreOwner: ViewModelStoreOwner,
    onSongClick: (id: String) -> Unit
) {
    val viewModel: FavoriteListViewModel = koinViewModel(viewModelStoreOwner = viewModelStoreOwner)

    val state by viewModel.state.collectAsStateWithLifecycle()

    FavoriteListScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onSongClick = onSongClick
    )
}

@Composable
private fun FavoriteListScreen(
    state: FavoriteListStateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onSongClick: (id: String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
    ) {
        when {
            state.progress -> {
                Progress()
            }

            state.error -> {
                ErrorLayout(onRetry = onRetry)
            }

            state.favorites != null && state.favorites.songs.isEmpty() -> {
                EmptyLayout(
                    modifier = Modifier.verticalScroll(
                        state = rememberScrollState()
                    )
                )
            }

            state.favorites?.songs != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.favorites.songs,
                        contentType = { "song" },
                        key = { "song_${it.id}" }
                    ) { song ->
                        FavoriteSongItem(
                            song = song,
                            onClick = { onSongClick(song.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress() {
    Column {
        repeat(10) {
            ListItem(
                headlineContent = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SkeletonScope.SkeletonItem(modifier = Modifier.size(width = 130.dp, height = 16.dp))
                        SkeletonScope.SkeletonItem(modifier = Modifier.size(width = 200.dp, height = 16.dp))
                    }
                },
                leadingContent = {
                    SkeletonScope.SkeletonItem(modifier = Modifier.size(48.dp))
                }
            )
        }
    }
}

@Composable
private fun FavoriteSongItem(
    song: FavoriteSongUi,
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
private fun FavoriteListScreenPreview() {
    MaterialTheme {
        val songs = listOf(
            FavoriteSongUi(
                id = "1",
                title = "Best song in the world 1",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            FavoriteSongUi(
                id = "2",
                title = "Best song in the world 2",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            FavoriteSongUi(
                id = "3",
                title = "Best song in the world 3",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            FavoriteSongUi(
                id = "4",
                title = "Best song in the world 4",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
            FavoriteSongUi(
                id = "5",
                title = "Best song in the world 5",
                artist = "Best artist in the world 1",
                artworkUrl = null
            ),
        )
        val state = FavoriteListStateUi(
            progress = true,
            isRefreshing = false,
            error = false,
            favorites = FavoritesUi(
                songs = songs
            )
        )
        FavoriteListScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onSongClick = {}
        )
    }
}