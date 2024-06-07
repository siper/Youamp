package ru.stersh.youamp.feature.playlists.ui

import ru.stersh.youamp.feature.playlists.domain.Playlist


internal fun Playlist.toUi(): PlaylistUi {
    return PlaylistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl
    )
}