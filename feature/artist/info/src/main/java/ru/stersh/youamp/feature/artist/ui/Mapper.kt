package ru.stersh.youamp.feature.artist.ui

import ru.stersh.youamp.core.ui.AlbumUi
import ru.stersh.youamp.feature.artist.domain.ArtistAlbum
import ru.stersh.youamp.feature.artist.domain.ArtistInfo

internal fun ArtistInfo.toUi(): ArtistInfoUi {
    return ArtistInfoUi(
        artworkUrl = artworkUrl,
        name = name,
        albums = albums.map { it.toUi() }
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