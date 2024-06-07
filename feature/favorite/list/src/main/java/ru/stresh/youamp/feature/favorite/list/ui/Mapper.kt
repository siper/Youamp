package ru.stresh.youamp.feature.favorite.list.ui

import ru.stresh.youamp.feature.favorite.list.domain.FavoriteSong
import ru.stresh.youamp.feature.favorite.list.domain.Favorites

internal fun Favorites.toUi(): FavoritesUi {
    return FavoritesUi(
        songs = songs.map { it.toUi() }
    )
}

private fun FavoriteSong.toUi() = FavoriteSongUi(
    id = id,
    title = title,
    artist = artist,
    artworkUrl = artworkUrl
)