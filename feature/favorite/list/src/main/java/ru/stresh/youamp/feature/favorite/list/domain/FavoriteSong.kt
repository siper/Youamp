package ru.stresh.youamp.feature.favorite.list.domain

internal data class FavoriteSong(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)
