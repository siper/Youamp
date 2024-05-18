package ru.stresh.youamp.feature.player.screen.ui

import ru.stersh.youamp.shared.player.mode.RepeatMode
import ru.stersh.youamp.shared.player.mode.ShuffleMode

internal fun RepeatMode.toUi(): RepeatModeUi {
    return when (this) {
        RepeatMode.One -> RepeatModeUi.One
        RepeatMode.All -> RepeatModeUi.All
        RepeatMode.Disabled -> RepeatModeUi.Disabled
    }
}

internal fun ShuffleMode.toUi(): ShuffleModeUi {
    return when (this) {
        ShuffleMode.Enabled -> ShuffleModeUi.Enabled
        ShuffleMode.Disabled -> ShuffleModeUi.Disabled
    }
}

internal fun RepeatModeUi.toDomain(): RepeatMode {
    return when (this) {
        RepeatModeUi.One -> RepeatMode.One
        RepeatModeUi.All -> RepeatMode.All
        RepeatModeUi.Disabled -> RepeatMode.Disabled
    }
}

internal fun ShuffleModeUi.toDomain(): ShuffleMode {
    return when (this) {
        ShuffleModeUi.Enabled -> ShuffleMode.Enabled
        ShuffleModeUi.Disabled -> ShuffleMode.Disabled
    }
}