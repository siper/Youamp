package ru.stersh.youamp.feature.personal.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.personal.domain.Album
import ru.stersh.youamp.feature.personal.domain.Artist
import ru.stersh.youamp.feature.personal.domain.Personal
import ru.stersh.youamp.feature.personal.domain.Playlist
import ru.stersh.youamp.feature.personal.domain.Song

internal fun Song.toUi(): SongUi {
    return SongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Album.toUi(): AlbumUi {
    return AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Playlist.toUi(): PlaylistUi {
    return PlaylistUi(
        id = id,
        title = name,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Artist.toUi(): ArtistUi {
    return ArtistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Personal.toUi(): PersonalDataUi {
    return PersonalDataUi(
        songs = songs
            .map { it.toUi() }
            .chunked(3)
            .map { it.toPersistentList() }
            .toPersistentList(),
        playlists = playlists
            .map { it.toUi() }
            .toPersistentList(),
        albums = albums
            .map { it.toUi() }
            .toPersistentList(),
        artists = artists
            .map { it.toUi() }
            .toPersistentList()
    )
}