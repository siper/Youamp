package ru.stersh.youamp.feature.album.ui

import kotlinx.collections.immutable.toImmutableList
import ru.stersh.youamp.feature.album.domain.AlbumInfo
import ru.stersh.youamp.feature.album.domain.AlbumSong

internal fun AlbumInfo.toUi(): AlbumInfoUi {
    return AlbumInfoUi(
        title = title,
        artist = artist,
        year = year,
        artworkUrl = artworkUrl,
        isFavorite = isFavorite,
        songs = songs
            .map { it.toUi() }
            .toImmutableList()
    )
}

internal fun AlbumSong.toUi(): AlbumSongUi {
    return AlbumSongUi(
        id = id,
        title = title,
        track = track,
        artist = artist,
        duration = duration
    )
}