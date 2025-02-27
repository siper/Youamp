package ru.stresh.youamp.core.player

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

internal fun MediaItem.toCommon(): ru.stresh.youamp.core.player.MediaItem {
    return MediaItem(
        id = mediaId,
        title = mediaMetadata.title.toString(),
        url = requestMetadata.mediaUri.toString(),
        artist = mediaMetadata.artist?.toString(),
        album = mediaMetadata.albumTitle?.toString(),
        artworkUrl = mediaMetadata.artworkUri?.toString()
    )
}

internal fun ru.stresh.youamp.core.player.MediaItem.toPlatform(): MediaItem {
    return MediaItem.Builder()
        .setUri(url)
        .setMediaId(id)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artist)
                .setArtworkUri(artworkUrl?.toUri())
                .build()
        )
        .build()
}