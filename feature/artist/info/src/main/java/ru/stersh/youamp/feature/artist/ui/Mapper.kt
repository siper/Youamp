package ru.stersh.youamp.feature.artist.ui

import kotlinx.collections.immutable.toImmutableList
import ru.stersh.youamp.feature.artist.domain.ArtistAlbum
import ru.stersh.youamp.feature.artist.domain.ArtistInfo

internal fun ArtistInfo.toUi(): ArtistInfoUi {
    return ArtistInfoUi(
        artworkUrl = artworkUrl,
        name = name,
        isFavorite = isFavorite,
        albums = albums
            .map { it.toUi() }
            .toImmutableList()
    )
}

internal fun ArtistAlbum.toUi(): AlbumUi {
    return AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}