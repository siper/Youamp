package ru.stresh.youamp.feature.song.random.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stresh.youamp.shared.song.random.Song

internal fun List<Song>.toUi(): DataUi {
    return DataUi(
        songs = map { it.toUi() }.toPersistentList()
    )
}

private fun Song.toUi() = SongUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)