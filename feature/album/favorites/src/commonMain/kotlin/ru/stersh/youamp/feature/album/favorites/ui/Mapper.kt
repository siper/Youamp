package ru.stersh.youamp.feature.album.favorites.ui

import kotlinx.collections.immutable.toImmutableList
import ru.stersh.youamp.feature.album.favorites.domain.Album
import ru.stersh.youamp.feature.album.favorites.domain.Favorites

internal fun Favorites.toUi(): DataUi {
    return DataUi(
        albums = albums
            .map { it.toUi() }
            .toImmutableList()
    )
}

private fun Album.toUi() = AlbumUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)