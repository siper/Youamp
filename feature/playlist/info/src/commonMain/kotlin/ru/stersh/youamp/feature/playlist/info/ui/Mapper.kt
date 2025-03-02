package ru.stersh.youamp.feature.playlist.info.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistInfo
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistSong

internal fun PlaylistInfo.toUi(): PlaylistInfoUi {
    return PlaylistInfoUi(
        artworkUrl = artworkUrl,
        title = title,
        songs = songs.toUi()
    )
}

internal fun List<PlaylistSong>.toUi(): ImmutableList<PlaylistSongUi> = map { it.toUi() }.toPersistentList()

internal fun PlaylistSong.toUi(): PlaylistSongUi {
    return PlaylistSongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isCurrent = isCurrent,
        isPlaying = isPlaying
    )
}