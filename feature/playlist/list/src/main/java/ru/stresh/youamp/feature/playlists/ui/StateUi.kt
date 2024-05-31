package ru.stresh.youamp.feature.playlists.ui

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface StateUi {
    @Immutable
    data class Content(val isRefreshing: Boolean, val items: List<PlaylistUi>) : StateUi

    @Immutable
    data object Progress : StateUi
}