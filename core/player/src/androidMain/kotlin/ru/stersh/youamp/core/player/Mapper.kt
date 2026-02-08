package ru.stersh.youamp.core.player

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

fun MediaItem.toCommon(): ru.stersh.youamp.core.player.MediaItem =
    MediaItem(
        id = mediaId,
        title = mediaMetadata.title.toString(),
        url = requestMetadata.mediaUri.toString(),
        artist = mediaMetadata.artist?.toString(),
        album = mediaMetadata.albumTitle?.toString(),
        artworkUrl = mediaMetadata.artworkUri?.toString(),
    )

fun ru.stersh.youamp.core.player.MediaItem.toPlatform(): MediaItem =
    MediaItem
        .Builder()
        .setUri(url)
        .setMediaId(id)
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(title)
                .setArtist(artist)
                .setArtworkUri(artworkUrl?.toUri())
                .build(),
        ).build()
