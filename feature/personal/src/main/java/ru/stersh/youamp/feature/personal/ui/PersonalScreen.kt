package ru.stersh.youamp.feature.personal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
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
import ru.stersh.youamp.core.ui.YouampPlayerTheme
import ru.stersh.youamp.feature.personal.R
import ru.stersh.youamp.feature.personal.ui.components.AlbumPersonalItem
import ru.stersh.youamp.feature.personal.ui.components.ArtistItem
import ru.stersh.youamp.feature.personal.ui.components.PersonalAlbumUi
import ru.stersh.youamp.feature.personal.ui.components.PersonalArtistUi
import ru.stersh.youamp.feature.personal.ui.components.PersonalSongUi
import ru.stersh.youamp.feature.personal.ui.components.PlaylistItem
import ru.stersh.youamp.feature.personal.ui.components.PlaylistUi
import ru.stersh.youamp.feature.personal.ui.components.SectionTitle
import ru.stersh.youamp.feature.personal.ui.components.SongCardItem
import ru.stersh.youamp.shared.player.queue.AudioSource

@Composable
fun PersonalScreen(
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
) {
    val viewModel: PersonalViewModel = koinViewModel()
    val state: PersonalScreenStateUi by viewModel.state.collectAsStateWithLifecycle()

    PersonalScreen(
        state = state,
        onPlayPauseAudioSource = viewModel::onPlayPauseAudioSource,
        onSongClick = onSongClick,
        onAlbumClick = onAlbumClick,
        onArtistClick = onArtistClick,
        onPlaylistClick = onPlaylistClick
    )
}

@Composable
private fun PersonalScreen(
    state: PersonalScreenStateUi,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
) {
    if (state.data != null) {
        Content(
            data = state.data,
            onPlayPauseAudioSource = onPlayPauseAudioSource,
            onSongClick = onSongClick,
            onAlbumClick = onAlbumClick,
            onArtistClick = onArtistClick,
            onPlaylistClick = onPlaylistClick
        )
    }
}

@Composable
private fun Content(
    data: PersonalDataUi,
    onPlayPauseAudioSource: (source: AudioSource) -> Unit,
    onSongClick: (id: String) -> Unit,
    onAlbumClick: (id: String) -> Unit,
    onArtistClick: (id: String) -> Unit,
    onPlaylistClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        if (data.playlists.isNotEmpty()) {
            item {
                SectionTitle(title = stringResource(R.string.playlists_title))
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
                SectionTitle(title = stringResource(R.string.songs_title))
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
                                            song = item,
                                            onPlayPauseClick = {
                                                onPlayPauseAudioSource(AudioSource.Song(item.id))
                                            },
                                            onClick = {
                                                onSongClick(item.id)
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
                SectionTitle(title = stringResource(R.string.albums_title))
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
                SectionTitle(title = stringResource(R.string.artists_title))
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
    val data: PersonalDataUi? = null
)

@Immutable
internal data class PersonalDataUi(
    val songs: List<List<PersonalSongUi>> = emptyList(),
    val playlists: List<PlaylistUi> = emptyList(),
    val albums: List<PersonalAlbumUi> = emptyList(),
    val artists: List<PersonalArtistUi> = emptyList(),
)

@Composable
@Preview
private fun PersonalScreenPreview() {
    YouampPlayerTheme {
        val state = PersonalScreenStateUi(
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
//                songs = listOf(
//                    listOf(
//                        SongUi(
//                            id = "Indra",
//                            title = "Britanny",
//                            artist = null,
//                            artworkUrl = null
//                        ),
//                        SongUi(
//                            id = "Raffaele",
//                            title = "Brittny",
//                            artist = null,
//                            artworkUrl = null
//                        ),
//                        SongUi(
//                            id = "Patsy",
//                            title = "Brittiney",
//                            artist = null,
//                            artworkUrl = null
//                        )
//                    ),
//                    listOf(
//                        SongUi(
//                            id = "Julieta",
//                            title = "Tykia",
//                            artist = null,
//                            artworkUrl = null
//                        ),
//                        SongUi(
//                            id = "Kofi",
//                            title = "Lyla",
//                            artist = null,
//                            artworkUrl = null
//                        )
//                    )
//                ),
//                albums = listOf(
//                    AlbumUi(
//                        id = "Kashif",
//                        title = "Tremaine",
//                        artist = "Slipknot",
//                        artworkUrl = null
//                    )
//                )
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
            onPlayPauseAudioSource = {},
            onSongClick = {},
            onAlbumClick = {},
            onArtistClick = {},
            onPlaylistClick = {}
        )
    }
}