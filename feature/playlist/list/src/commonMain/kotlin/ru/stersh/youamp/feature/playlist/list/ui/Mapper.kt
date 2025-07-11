package ru.stersh.youamp.feature.playlist.list.ui

import ru.stersh.youamp.feature.playlist.list.domain.Playlist

internal fun Playlist.toUi(): PlaylistUi =
    PlaylistUi(
        id = id,
        name = name,
        artworkUrl = artworkUrl,
    )
