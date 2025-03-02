package ru.stresh.youamp.feature.explore.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stresh.youamp.feature.explore.domain.Explore
import ru.stresh.youamp.feature.explore.domain.Song

internal fun Explore.toUi(): DataUi {
    return DataUi(
        randomSongs = randomSongs
            .map { it.toUi() }
            .chunked(3)
            .map { it.toPersistentList() }
            .toPersistentList()
    )
}

private fun Song.toUi(): SongUi {
    return SongUi(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl,
        isPlaying = isPlaying
    )
}