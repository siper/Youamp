package ru.stersh.youamp.feature.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.feature.search.R
import ru.stersh.youamp.shared.player.queue.AudioSource


@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onOpenSongInfo: (songId: String) -> Unit,
    onOpenAlbumInfo: (albumId: String) -> Unit,
    onOpenArtistInfo: (albumId: String) -> Unit
) {
    val viewModel: SearchViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onLoadMoreSongs = viewModel::onLoadMoreSongs,
        onLoadMoreAlbums = viewModel::onLoadMoreAlbums,
        onLoadMoreArtists = viewModel::onLoadMoreArtists,
        onSearchQueryChange = viewModel::onQueryChange,
        onCloseClick = onBack,
        onPlay = viewModel::play,
        onAddToQueue = viewModel::addToQueue,
        onOpenSongInfo = onOpenSongInfo,
        onOpenAlbumInfo = onOpenAlbumInfo,
        onOpenArtistInfo = onOpenArtistInfo
    )
}

@Composable
private fun SearchScreen(
    state: SearchStateUi,
    onLoadMoreSongs: () -> Unit,
    onLoadMoreAlbums: () -> Unit,
    onLoadMoreArtists: () -> Unit,
    onSearchQueryChange: (query: String) -> Unit,
    onCloseClick: () -> Unit,
    onPlay: (source: AudioSource) -> Unit,
    onAddToQueue: (source: AudioSource) -> Unit,
    onOpenSongInfo: (songId: String) -> Unit,
    onOpenAlbumInfo: (albumId: String) -> Unit,
    onOpenArtistInfo: (albumId: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = "request_focus") {
        focusRequester.requestFocus()
    }
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
            Row(
                modifier = Modifier.height(72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onCloseClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.close_search_title),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                var value by rememberSaveable { mutableStateOf("") }
                val cursorColor = MaterialTheme.colorScheme.onSurface
                BasicTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        onSearchQueryChange(it)
                    },
                    cursorBrush = remember { SolidColor(cursorColor) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextFiled ->
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.search_hint_title),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            innerTextFiled()
                        }
                    }
                )

                IconButton(
                    onClick = {
                        value = ""
                        onSearchQueryChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.clear_search_title),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            when {
                state.progress -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                !state.progress -> {
                    val listState = rememberLazyListState()
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        if (state.songs.isNotEmpty()) {
                            item(
                                contentType = "title",
                                key = "songs_title"
                            ) {
                                Text(
                                    text = stringResource(R.string.songs_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(
                                items = state.songs,
                                contentType = { "song" },
                                key = { "song_${it.id}" }
                            ) { result ->
                                SongItem(
                                    item = result,
                                    onMoreClick = { onOpenSongInfo(result.id) }
                                )
                            }
                            if (state.hasMoreSongs) {
                                item(
                                    key = "songs_load_more",
                                    contentType = "load_more"
                                ) {
                                    LoadMoreButton(
                                        onLoadMore = { onLoadMoreSongs() },
                                        modifier = Modifier.fillParentMaxWidth()
                                    )
                                }
                            }
                        }
                        if (state.albums.isNotEmpty()) {
                            item(
                                contentType = "title",
                                key = "albums_title"
                            ) {
                                Text(
                                    text = stringResource(R.string.albums_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(
                                items = state.albums,
                                contentType = { "album" },
                                key = { "album_${it.id}" }
                            ) { result ->
                                AlbumItem(
                                    item = result,
                                    onPlay = {
                                        onPlay(AudioSource.Album(it))
                                    },
                                    onAddToQueue = {
                                        onAddToQueue(AudioSource.Album(it))
                                    },
                                    onOpenInfo = onOpenAlbumInfo
                                )
                            }
                            if (state.hasMoreAlbums) {
                                item(
                                    key = "albums_load_more",
                                    contentType = "load_more"
                                ) {
                                    LoadMoreButton(
                                        onLoadMore = { onLoadMoreAlbums() },
                                        modifier = Modifier.fillParentMaxWidth()
                                    )
                                }
                            }
                        }
                        if (state.artists.isNotEmpty()) {
                            item(
                                contentType = "title",
                                key = "artists_title"
                            ) {
                                Text(
                                    text = stringResource(R.string.artists_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                            items(
                                items = state.artists,
                                contentType = { "artist" },
                                key = { "artist_${it.id}" }
                            ) { result ->
                                ArtistItem(
                                    item = result,
                                    onPlay = {
                                        onPlay(AudioSource.Artist(it))
                                    },
                                    onAddToQueue = {
                                        onAddToQueue(AudioSource.Artist(it))
                                    },
                                    onOpenInfo = onOpenArtistInfo
                                )
                            }
                            if (state.hasMoreArtists) {
                                item(
                                    key = "artists_load_more",
                                    contentType = "load_more"
                                ) {
                                    LoadMoreButton(
                                        onLoadMore = { onLoadMoreArtists() },
                                        modifier = Modifier.fillParentMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadMoreButton(
    onLoadMore: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        SuggestionChip(
            onClick = { onLoadMore() },
            label = {
                Text(text = stringResource(R.string.load_more_title))
            },
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
private fun SearchScreenPreview() {
    SearchScreen(
        state = SearchStateUi(
            progress = false,
            songs = persistentListOf(
                Song(
                    id = "1",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                ),
                Song(
                    id = "2",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                ),
                Song(
                    id = "3",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                )
            ),
            albums = persistentListOf(
                Album(
                    id = "1",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                ),
                Album(
                    id = "2",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                ),
                Album(
                    id = "3",
                    title = "Coolest song in the world",
                    artist = "Coolest artist in the world",
                    artworkUrl = null
                )
            ),
            artists = persistentListOf(
                Artist(
                    id = "1",
                    name = "Coolest song in the world",
                    artworkUrl = null
                ),
                Artist(
                    id = "2",
                    name = "Coolest song in the world",
                    artworkUrl = null
                ),
                Artist(
                    id = "3",
                    name = "Coolest song in the world",
                    artworkUrl = null
                ),
            )
        ),
        onLoadMoreSongs = {},
        onLoadMoreAlbums = {},
        onLoadMoreArtists = {},
        onSearchQueryChange = {},
        onCloseClick = {},
        onPlay = {},
        onAddToQueue = {},
        onOpenSongInfo = {},
        onOpenAlbumInfo = {},
        onOpenArtistInfo = {}
    )
}