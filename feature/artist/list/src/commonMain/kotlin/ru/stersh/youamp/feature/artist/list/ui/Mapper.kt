package ru.stersh.youamp.feature.artist.list.ui

import ru.stersh.youamp.feature.artist.list.domain.Artist

internal fun Artist.toUi(): ArtistUi =
    ArtistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl,
    )
