package ru.stresh.youamp.feature.artist.favorites.ui

import ru.stresh.youamp.feature.artist.favorites.domain.Artist
import ru.stresh.youamp.feature.artist.favorites.domain.Favorites

internal fun Favorites.toUi(): DataUi {
    return DataUi(
        artists = artists.map { it.toUi() }
    )
}

private fun Artist.toUi() = ArtistUi(
    id = id,
    name = name,
    artworkUrl = artworkUrl
)