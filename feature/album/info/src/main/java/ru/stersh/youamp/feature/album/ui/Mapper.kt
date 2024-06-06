package ru.stersh.youamp.feature.album.ui

import ru.stersh.youamp.feature.album.domain.AlbumInfo
import ru.stersh.youamp.feature.album.domain.AlbumSong

internal fun AlbumInfo.toUi(): AlbumInfoUi {
    return AlbumInfoUi(
        title = title,
        artist = artist,
        year = year,
        artworkUrl = artworkUrl,
        songs = songs.map { it.toUi() }
    )
}

internal fun AlbumSong.toUi(): AlbumSongUi {
    return AlbumSongUi(
        id = id,
        title = title,
        duration = duration
    )
}