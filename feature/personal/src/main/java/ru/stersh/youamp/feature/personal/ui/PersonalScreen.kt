package ru.stersh.youamp.feature.personal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.stersh.youamp.core.ui.EmptyLayout
import ru.stersh.youamp.core.ui.ErrorLayout
import ru.stersh.youamp.core.ui.LayoutStateUi
import ru.stersh.youamp.core.ui.PlayButton
import ru.stersh.youamp.core.ui.SectionTitle
import ru.stersh.youamp.core.ui.SkeletonLayout
import ru.stersh.youamp.core.ui.SongCardItem
import ru.stersh.youamp.core.ui.StateLayout
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.personal.R
import ru.stersh.youamp.feature.personal.ui.components.AlbumPersonalItem
import ru.stersh.youamp.feature.personal.ui.components.ArtistItem
import ru.stersh.youamp.feature.personal.ui.components.PersonalAlbumUi
import ru.stersh.youamp.feature.personal.ui.components.PersonalArtistUi
import ru.stersh.youamp.feature.personal.ui.components.PlaylistItem
import ru.stersh.youamp.feature.personal.ui.components.PlaylistUi
import ru.stersh.youamp.shared.player.queue.AudioSource

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
    val state: PersonalScreenStateUi by viewModel.state.collectAsStateWithLifecycle()

    PersonalScreen(
        state = state,
        onRetry = viewModel::retry,
        onPlayPauseAudioSource = viewModel::onPlayPauseAudioSource,
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
    state: PersonalScreenStateUi,
    onRetry: () -> Unit,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
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
    StateLayout(
        state = layoutState,
        content = {
            Content(
                data = state.data,
                onPlayPauseAudioSource = onPlayPauseAudioSource,
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
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
                SkeletonItem(
                    modifier = Modifier.size(
                        336.dp,
                        64.dp
                    )
                )
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
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
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
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (data.playlists.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.playlists_title),
                    onClick = onPlaylistsClick
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    data.playlists.forEach {
                        item {
                            PlaylistItem(
                                playlist = it,
                                onClick = {
                                    onPlaylistClick(it)
                                },
                                onPlayPauseClick = {
                                    onPlayPauseAudioSource(AudioSource.Playlist(it.id))
                                },
                                modifier = Modifier.requiredWidth(160.dp)
                            )
                        }
                    }
                }
            }
        }
        if (data.songs.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.songs_title),
                    onClick = onFavoriteSongsClick
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
                    data
                        .songs
                        .forEach { songChunk ->
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.requiredWidth(336.dp)
                                ) {
                                    songChunk.forEach { item ->
                                        SongCardItem(
                                            title = item.title,
                                            artist = item.artist,
                                            artworkUrl = item.artworkUrl,
                                            onClick = {
                                                onSongClick(item.id)
                                            },
                                            playButton = {
                                                PlayButton(
                                                    isPlaying = item.isPlaying,
                                                    onClick = {
                                                        onPlayPauseAudioSource(AudioSource.Song(item.id))
                                                    }
                                                )
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }
        if (data.albums.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.albums_title),
                    onClick = onFavoriteAlbumsClick
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    data
                        .albums
                        .forEach {
                            item {
                                AlbumPersonalItem(
                                    album = it,
                                    onPlayPauseClick = {
                                        onPlayPauseAudioSource(AudioSource.Album(it.id))
                                    },
                                    onClick = {
                                        onAlbumClick(it)
                                    },
                                    modifier = Modifier.requiredWidth(160.dp)
                                )
                            }
                        }
                }
            }
        }
        if (data.artists.isNotEmpty()) {
            item {
                SectionTitle(
                    title = stringResource(R.string.artists_title),
                    onClick = onFavoriteArtistsClick
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    data
                        .artists
                        .forEach {
                            item {
                                ArtistItem(
                                    artist = it,
                                    onPlayPauseClick = {
                                        onPlayPauseAudioSource(AudioSource.Artist(it.id))
                                    },
                                    onClick = {
                                        onArtistClick(it)
                                    },
                                    modifier = Modifier.requiredWidth(160.dp)
                                )
                            }
                        }
                }
            }
        }
    }
}

@Immutable
internal data class PersonalScreenStateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val data: PersonalDataUi? = null
)

@Immutable
internal data class PersonalDataUi(
    val songs: List<List<PersonalSongUi>> = emptyList(),
    val playlists: List<PlaylistUi> = emptyList(),
    val albums: List<PersonalAlbumUi> = emptyList(),
    val artists: List<PersonalArtistUi> = emptyList(),
) {

    val isEmpty: Boolean
        get() = songs.isEmpty() && playlists.isEmpty() && albums.isEmpty() && artists.isEmpty()
}

@Composable
@Preview
private fun PersonalScreenPreview() {
    YouampPlayerTheme {
        val state = PersonalScreenStateUi(
            progress = false,
            error = false,
            data = PersonalDataUi(
                playlists = listOf(
                    PlaylistUi(
                        id = "Verna",
                        name = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        name = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        name = "Sedric",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PlaylistUi(
                        id = "Verna",
                        name = "Sedric",
                        artworkUrl = null,
                        isPlaying = true
                    )
                ),
                songs = listOf(
                    listOf(
                        PersonalSongUi(
                            id = "Indra",
                            title = "Britanny",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        PersonalSongUi(
                            id = "Raffaele",
                            title = "Brittny",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        PersonalSongUi(
                            id = "Patsy",
                            title = "Brittiney",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        )
                    ),
                    listOf(
                        PersonalSongUi(
                            id = "Julieta",
                            title = "Tykia",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        PersonalSongUi(
                            id = "Kofi",
                            title = "Lyla",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        ),
                        PersonalSongUi(
                            id = "Kofi",
                            title = "Lyla",
                            artist = null,
                            artworkUrl = null,
                            isPlaying = false
                        )
                    )
                ),
                albums = listOf(
                    PersonalAlbumUi(
                        id = "Kashif",
                        title = "Tremaine",
                        artist = "Slipknot",
                        artworkUrl = null,
                        isPlaying = false
                    )
                ),
                artists = listOf(
                    PersonalArtistUi(
                        id = "Soloman",
                        name = "Eliana",
                        artworkUrl = null,
                        isPlaying = false
                    ),
                    PersonalArtistUi(
                        id = "Tari",
                        name = "Shamir",
                        artworkUrl = null,
                        isPlaying = false
                    )
                )
            ),
        )
        PersonalScreen(
            state = state,
            onRetry = {},
            onPlayPauseAudioSource = {},
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