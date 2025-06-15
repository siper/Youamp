package ru.stersh.youamp.feature.player.mini.ui

internal data class StateUi(
    val data: PlayerDataUi? = null,
    val invisible: Boolean = true,
)

internal data class PlayerDataUi(
    val title: String?,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean,
    val progress: Float,
)
