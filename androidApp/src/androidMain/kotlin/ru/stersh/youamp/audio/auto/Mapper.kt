package ru.stersh.youamp.audio.auto

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import ru.stersh.youamp.audio.auto.MediaLibrary.LIBRARY_PLAYLIST_PREFIX

internal fun Auto.Song.toMediaItem(): MediaItem {
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

internal fun Auto.Playlist.toMediaItem(): MediaItem {
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