package ru.stersh.youamp.feature.albums.ui

import ru.stersh.youamp.core.ui.AlbumUi
import ru.stersh.youamp.feature.albums.domain.Album

internal fun List<Album>.toUi(): List<AlbumUi> = map { it.toUi() }

internal fun Album.toUi(): AlbumUi {
    return AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}