package ru.stresh.youamp.feature.favorite.list.ui

import ru.stresh.youamp.feature.favorite.list.domain.Favorites
import ru.stresh.youamp.feature.favorite.list.domain.Song

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