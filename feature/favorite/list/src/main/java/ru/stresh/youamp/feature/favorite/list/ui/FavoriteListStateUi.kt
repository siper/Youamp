package ru.stresh.youamp.feature.favorite.list.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class FavoriteListStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val favorites: FavoritesUi? = null
)