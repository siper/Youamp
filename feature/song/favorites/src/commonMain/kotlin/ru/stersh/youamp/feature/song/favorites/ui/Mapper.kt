package ru.stersh.youamp.feature.song.favorites.ui

import kotlinx.collections.immutable.toPersistentList
import ru.stersh.youamp.feature.song.favorites.domain.Favorites
import ru.stersh.youamp.feature.song.favorites.domain.Song

internal fun Favorites.toUi(): DataUi {
    return DataUi(
        songs = songs
            .map { it.toUi() }
            .toPersistentList()
    )
}

private fun Song.toUi() = SongUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)