package ru.stresh.youamp.feature.artists.ui

internal sealed interface StateUi {
    data class Content(val isRefreshing: Boolean, val items: List<ArtistUi>) : StateUi
    data object Progress : StateUi
}