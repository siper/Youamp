package ru.stersh.youamp.feature.player.screen.ui

internal data class StateUi(
    val artworkUrl: String? = null,
    val title: String? = null,
    val artist: String? = null,
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentTime: String? = "00:00",
    val totalTime: String? = "00:00",
    val repeatMode: RepeatModeUi = RepeatModeUi.Disabled,
    val shuffleMode: ShuffleModeUi = ShuffleModeUi.Disabled,
    val isFavorite: Boolean = false,
)
