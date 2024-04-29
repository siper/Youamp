package ru.stresh.youamp.feature.albums.ui

internal sealed interface StateUi {
    data class Content(val isRefreshing: Boolean, val items: List<AlbumUi>) : StateUi
    data object Progress : StateUi
}

internal val StateUi.isRefreshing: Boolean
    get() = (this as? StateUi.Content)?.isRefreshing == true