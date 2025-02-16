package ru.stersh.youamp.shared.player.android

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ru.stersh.youamp.shared.player.android.MediaLibrary.LIBRARY_PLAYLIST_PREFIX
import ru.stersh.youamp.shared.player.library.MediaLibrary

internal fun MediaLibrary.Song.toMediaItem(): MediaItem {
    return MediaItem
        .Builder()
        .setMediaId(id)
        .setUri(streamUrl)
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(title)
                .setArtist(artist)
                .setArtworkUri(coverUrl?.toUri())
                .setIsPlayable(true)
                .setIsBrowsable(false)
                .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
                .build()
        )
        .build()
}

internal fun MediaLibrary.Playlist.toMediaItem(): MediaItem {
    return MediaItem
        .Builder()
        .setMediaId(LIBRARY_PLAYLIST_PREFIX + id)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtworkUri(coverUrl?.toUri())
                .setIsPlayable(true)
                .setIsBrowsable(true)
                .setMediaType(MediaMetadata.MEDIA_TYPE_PLAYLIST)
                .build()
        )
        .build()
}