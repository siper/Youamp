package ru.stersh.youamp.feature.album.info.ui

import kotlinx.collections.immutable.toImmutableList
import ru.stersh.youamp.feature.album.info.domain.AlbumInfo
import ru.stersh.youamp.feature.album.info.domain.AlbumSong

internal fun AlbumInfo.toUi(): AlbumInfoUi =
    AlbumInfoUi(
        title = title,
        artist = artist,
        year = year,
        artworkUrl = artworkUrl,
        isFavorite = isFavorite,
        songs =
            songs
                .map { it.toUi() }
                .toImmutableList(),
    )

internal fun AlbumSong.toUi(): AlbumSongUi =
    AlbumSongUi(
        id = id,
        title = title,
        track = track,
        artist = artist,
        duration = duration,
    )
