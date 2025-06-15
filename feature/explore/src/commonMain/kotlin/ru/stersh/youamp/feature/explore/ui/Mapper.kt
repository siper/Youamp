package ru.stersh.youamp.feature.explore.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.explore.domain.Explore
import ru.stersh.youamp.feature.explore.domain.Song

internal fun Explore.toUi(): DataUi =
    DataUi(
        randomSongs =
            randomSongs
                .map { it.toUi() }
                .chunked(3)
                .map { it.toPersistentList() }
                .toPersistentList(),
    )

private fun Song.toUi(): SongUi =
    SongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying,
    )
