package ru.stresh.youamp.feature.favorite.list.ui

import androidx.compose.runtime.Immutable

@Immutable
internal class FavoriteSongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)