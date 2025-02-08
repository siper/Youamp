package ru.stersh.youamp.shared.player.utils

import androidx.media3.common.MediaItem
import androidx.media3.common.Player

val Player.mediaItems: List<MediaItem>
    get() = if (mediaItemCount > 0) {
        (0 until mediaItemCount).map { getMediaItemAt(it) }
    } else {
        emptyList()
    }
