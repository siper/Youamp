package ru.stresh.youamp.feature.library.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stresh.youamp.feature.library.domain.Album
import ru.stresh.youamp.feature.library.domain.Artist
import ru.stresh.youamp.feature.library.domain.Library

internal fun Library.toUi(): DataUi {
    return DataUi(
        artists = artists
            .map { it.toUi() }
            .toPersistentList(),
        albums = albums
            .map { it.toUi() }
            .toPersistentList(),
    )
}

private fun Album.toUi(): AlbumUi {
    return AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}

private fun Artist.toUi(): ArtistUi {
    return ArtistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}