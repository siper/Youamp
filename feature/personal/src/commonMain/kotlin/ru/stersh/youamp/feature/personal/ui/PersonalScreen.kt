package ru.stersh.youamp.feature.personal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PlaylistItem
import ru.stersh.youamp.core.ui.PlaylistItemDefaults
import ru.stersh.youamp.core.ui.PlaylistSkeleton
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
import youamp.feature.personal.generated.resources.Res
import youamp.feature.personal.generated.resources.albums_title
import youamp.feature.personal.generated.resources.artists_title
import youamp.feature.personal.generated.resources.playlists_title
import youamp.feature.personal.generated.resources.songs_title

@Composable
fun PersonalScreen(
    onPlaylistsClick: () -> Unit,
    onFavoriteSongsClick: () -> Unit,
    onFavoriteAlbumsClick: () -> Unit,
    onFavoriteArtistsClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
) {
    val viewModel: PersonalViewModel = koinViewModel()
    val state: StateUi by viewModel.state.collectAsStateWithLifecycle()

    PersonalScreen(
        state = state,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onSongClick = onSongClick,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onPlaylistClick = onPlaylistClick,
        onPlaylistsClick = onPlaylistsClick,
        onFavoriteSongsClick = onFavoriteSongsClick,
        onFavoriteAlbumsClick = onFavoriteAlbumsClick,
        onFavoriteArtistsClick = onFavoriteArtistsClick
    )
}

@Composable
private fun PersonalScreen(
    state: StateUi,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onPlaylistsClick: () -> Unit,
    onFavoriteSongsClick: () -> Unit,
    onFavoriteAlbumsClick: () -> Unit,
    onFavoriteArtistsClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
) {
    val layoutState = when {
        state.progress -> LayoutStateUi.Progress
        state.error -> LayoutStateUi.Error
        state.data?.isEmpty == true -> LayoutStateUi.Empty
        else -> LayoutStateUi.Content
    }
    PullToRefresh(
        isRefreshing = state.refreshing,
        onRefresh = onRefresh
    ) {
        StateLayout(
            state = layoutState,
            content = {
                Content(
                    data = state.data,
                    onSongClick = onSongClick,
                    onAlbumClick = onAlbumClick,
                    onArtistClick = onArtistClick,
                    onPlaylistClick = onPlaylistClick,
                    onPlaylistsClick = onPlaylistsClick,
                    onFavoriteSongsClick = onFavoriteSongsClick,
                    onFavoriteAlbumsClick = onFavoriteAlbumsClick,
                    onFavoriteArtistsClick = onFavoriteArtistsClick
                )
            },
            progress = {
                Progress()
            },
            error = {
                ErrorLayout(onRetry = onRetry)
            },
            empty = {
                EmptyLayout()
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
private fun Progress(modifier: Modifier = Modifier) {
    SkeletonLayout(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.padding(
                horizontal = 24.dp
            ),
            userScrollEnabled = false
        ) {
            item {
                SectionSkeleton()
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    repeat(3) {
                        item { PlaylistSkeleton() }
                    }
                }
            }
            item {
                SectionSkeleton()
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    repeat(2) {
                        item { SongCardChunkSkeleton() }
                    }
                }
            }
            item {
                SectionSkeleton()
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = false
                ) {
                    repeat(3) {
                        item { AlbumSkeleton() }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ProgressPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Progress()
        }
    }
}

@Composable
private fun Content(
    data: PersonalDataUi?,
    onPlaylistsClick: () -> Unit,
    onFavoriteSongsClick: () -> Unit,
    onFavoriteAlbumsClick: () -> Unit,
    onFavoriteArtistsClick: () -> Unit,
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (data == null) {
        return
    }
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (data.playlists.isNotEmpty()) {
            item {
                val playlistsState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.playlists_title),
                    onClick = onPlaylistsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(playlistsState)
                        }
                    }
                ) {
                    LazyRow(
                        state = playlistsState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        data.playlists.forEach {
                            item {
                                PlaylistItem(
                                    title = it.title,
                                    onClick = {
                                        onPlaylistClick(it.id)
                                    },
                                    modifier = Modifier.requiredWidth(PlaylistItemDefaults.Width)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (data.songs.isNotEmpty()) {
            item {
                val lazyListState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.songs_title),
                    onClick = onFavoriteSongsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(lazyListState)
                        }
                    }
                ) {
                    LazyRow(
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        data.songs.forEach { songChunk ->
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
        }
        if (data.albums.isNotEmpty()) {
            item {
                val lazyListState = rememberLazyListState()
                Section(
                    title = stringResource(Res.string.albums_title),
                    onClick = onFavoriteAlbumsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(lazyListState)
                        }
                    }
                ) {
                    LazyRow(
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        data.albums.forEach {
                            item {
                                AlbumItem(
                                    title = it.title,
                                    artist = it.artist,
                                    artworkUrl = it.artworkUrl,
                                    onClick = {
                                        onAlbumClick(it.id)
                                    },
                                    modifier = Modifier.requiredWidth(AlbumItemDefaults.Width)
                                )
                            }
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
                    onClick = onFavoriteArtistsClick,
                    actions = {
                        if (!currentPlatform.mobile) {
                            SectionScrollActions(lazyListState)
                        }
                    }
                ) {
                    LazyRow(
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        data.artists.forEach {
                            item {
                                ArtistItem(
                                    name = it.name,
                                    artworkUrl = it.artworkUrl,
                                    onClick = {
                                        onArtistClick(it.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PersonalScreenPreview() {
    YouampPlayerTheme {
        val state = StateUi(
            progress = false,
            error = false,
            data = PersonalDataUi(
                playlists = persistentListOf(
                    PlaylistUi(
                        id = "Verna",
                        title = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        title = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        title = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        title = "Sedric",
                        artworkUrl = null,
                        isPlaying = true
                    ),
                ),
                songs = persistentListOf(
                    persistentListOf(
                        SongUi(
                            id = "Indra",
                            title = "Britanny",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        SongUi(
                            id = "Raffaele",
                            title = "Brittny",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        SongUi(
                            id = "Patsy",
                            title = "Brittiney",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        )
                    ),
                    persistentListOf(
                        SongUi(
                            id = "Julieta",
                            title = "Tykia",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        SongUi(
                            id = "Kofi",
                            title = "Lyla",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        SongUi(
                            id = "Kofi",
                            title = "Lyla",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false,
                        )
                    )
                ),
                albums = persistentListOf(
                    AlbumUi(
                        id = "Kashif",
                        title = "Tremaine",
                        artist = "Slipknot",
                        artworkUrl = null,
                        isPlaying = false
                    )
                ),
                artists = persistentListOf(
                    ArtistUi(
                        id = "Soloman",
                        name = "Eliana",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    ArtistUi(
                        id = "Tari",
                        name = "Shamir",
                        artworkUrl = null,
                        isPlaying = false
                    )
                )
            )
        )
        PersonalScreen(
            state = state,
            onRetry = {},
            onRefresh = {},
            onSongClick = {},
            onAlbumClick = {},
            onArtistClick = {},
            onPlaylistClick = {},
            onPlaylistsClick = {},
            onFavoriteSongsClick = {},
            onFavoriteAlbumsClick = {},
            onFavoriteArtistsClick = {}
        )
    }
}
