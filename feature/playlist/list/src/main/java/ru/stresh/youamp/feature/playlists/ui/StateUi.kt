package ru.stresh.youamp.feature.playlists.ui

internal sealed interface StateUi {
    data class Content(val isRefreshing: Boolean, val items: List<PlaylistUi>) : StateUi
    data object Progress : StateUi
}