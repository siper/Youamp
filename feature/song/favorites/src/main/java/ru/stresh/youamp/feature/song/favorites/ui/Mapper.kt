package ru.stresh.youamp.feature.song.favorites.ui

import ru.stresh.youamp.feature.song.favorites.domain.Favorites
import ru.stresh.youamp.feature.song.favorites.domain.Song

internal fun Favorites.toUi(): DataUi {
    return DataUi(
        songs = songs.map { it.toUi() }
    )
}

private fun Song.toUi() = SongUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)