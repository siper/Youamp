package ru.stersh.youamp.feature.album.list.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.album.list.domain.Album

internal fun List<Album>.toUi(): ImmutableList<AlbumUi> = map { it.toUi() }.toPersistentList()

internal fun Album.toUi(): AlbumUi =
    AlbumUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
    )
