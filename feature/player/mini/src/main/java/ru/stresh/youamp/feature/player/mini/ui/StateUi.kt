package ru.stresh.youamp.feature.player.mini.ui

internal sealed interface StateUi {
    data class Content(
        val title: String?,
        val artist: String?,
        val artworkUrl: String?,
        val isPlaying: Boolean,
        val progress: Float,

        ) : StateUi

    data object Invisible : StateUi
}