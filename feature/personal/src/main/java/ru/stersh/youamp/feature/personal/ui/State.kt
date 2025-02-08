package ru.stersh.youamp.feature.personal.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


internal data class StateUi(
    val progress: Boolean = true,
    val refreshing: Boolean = false,
    val error: Boolean = false,
    val data: PersonalDataUi? = null
)

internal data class PersonalDataUi(
    val songs: ImmutableList<ImmutableList<SongUi>> = persistentListOf(),
    val playlists: ImmutableList<PlaylistUi> = persistentListOf(),
    val albums: ImmutableList<AlbumUi> = persistentListOf(),
    val artists: ImmutableList<ArtistUi> = persistentListOf(),
) {

    val isEmpty: Boolean
        get() = songs.isEmpty() && playlists.isEmpty() && albums.isEmpty() && artists.isEmpty()
}

internal data class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

internal data class PlaylistUi(
    val id: String,
    val title: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

internal data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

internal data class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)
