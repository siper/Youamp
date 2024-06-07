package ru.stersh.youamp.feature.artists.ui

import ru.stersh.youamp.feature.artists.domain.Artist

internal fun Artist.toUi(): ArtistUi {
    return ArtistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl
    )
}