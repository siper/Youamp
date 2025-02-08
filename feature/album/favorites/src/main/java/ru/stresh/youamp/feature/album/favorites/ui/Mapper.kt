package ru.stresh.youamp.feature.album.favorites.ui

import ru.stresh.youamp.feature.album.favorites.domain.Album
import ru.stresh.youamp.feature.album.favorites.domain.Favorites

internal fun Favorites.toUi(): DataUi {
    return DataUi(
        albums = albums.map { it.toUi() }
    )
}

private fun Album.toUi() = AlbumUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)