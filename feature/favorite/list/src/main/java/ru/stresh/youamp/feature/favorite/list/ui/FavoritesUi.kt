package ru.stresh.youamp.feature.favorite.list.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class FavoritesUi(
    val songs: List<FavoriteSongUi>
)
