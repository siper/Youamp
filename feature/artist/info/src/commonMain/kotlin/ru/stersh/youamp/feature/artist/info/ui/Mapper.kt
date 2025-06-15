package ru.stersh.youamp.feature.artist.info.ui

import kotlinx.collections.immutable.toImmutableList
import ru.stersh.youamp.feature.artist.info.domain.ArtistAlbum
import ru.stersh.youamp.feature.artist.info.domain.ArtistInfo

internal fun ArtistInfo.toUi(): ArtistInfoUi =
    ArtistInfoUi(
        artworkUrl = artworkUrl,
        name = name,
        isFavorite = isFavorite,
        albums =
            albums
                .map { it.toUi() }
                .toImmutableList(),
    )

internal fun ArtistAlbum.toUi(): AlbumUi =
    AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
    )
