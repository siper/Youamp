package ru.stersh.youamp.feature.personal.ui

import ru.stersh.youamp.feature.personal.domain.Album
import ru.stersh.youamp.feature.personal.domain.Artist
import ru.stersh.youamp.feature.personal.domain.Personal
import ru.stersh.youamp.feature.personal.domain.Playlist
import ru.stersh.youamp.feature.personal.domain.Song
import ru.stersh.youamp.feature.personal.ui.components.PersonalAlbumUi
import ru.stersh.youamp.feature.personal.ui.components.PersonalArtistUi
import ru.stersh.youamp.feature.personal.ui.components.PlaylistUi

internal fun Song.toUi(): PersonalSongUi {
    return PersonalSongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Album.toUi(): PersonalAlbumUi {
    return PersonalAlbumUi(
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
        name = name,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

internal fun Artist.toUi(): PersonalArtistUi {
    return PersonalArtistUi(
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
            .chunked(3),
        playlists = playlists.map { it.toUi() },
        albums = albums.map { it.toUi() },
        artists = artists.map { it.toUi() }
    )
}