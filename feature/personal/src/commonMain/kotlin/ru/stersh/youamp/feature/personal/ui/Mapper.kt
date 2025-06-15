package ru.stersh.youamp.feature.personal.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.personal.domain.Album
import ru.stersh.youamp.feature.personal.domain.Artist
import ru.stersh.youamp.feature.personal.domain.Personal
import ru.stersh.youamp.feature.personal.domain.Playlist
import ru.stersh.youamp.feature.personal.domain.Song

internal fun Song.toUi(): SongUi =
    SongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
    )

internal fun Album.toUi(): AlbumUi =
    AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
    )

internal fun Playlist.toUi(): PlaylistUi =
    PlaylistUi(
        id = id,
        title = name,
        artworkUrl = artworkUrl,
    )

internal fun Artist.toUi(): ArtistUi =
    ArtistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl,
    )

internal fun Personal.toUi(): PersonalDataUi =
    PersonalDataUi(
        songs =
            songs
                .map { it.toUi() }
                .chunked(3)
                .map { it.toPersistentList() }
                .toPersistentList(),
        playlists =
            playlists
                .map { it.toUi() }
                .toPersistentList(),
        albums =
            albums
                .map { it.toUi() }
                .toPersistentList(),
        artists =
            artists
                .map { it.toUi() }
                .toPersistentList(),
    )
