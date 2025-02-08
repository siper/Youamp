package ru.stersh.youamp.feature.albums.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.albums.domain.Album

internal fun List<Album>.toUi(): ImmutableList<AlbumUi> {
    return map { it.toUi() }.toPersistentList()
}

internal fun Album.toUi(): AlbumUi {
    return AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}