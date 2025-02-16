package ru.stersh.youamp.shared.player.utils

import androidx.core.net.toUri
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.StarRating
import ru.stersh.youamp.core.api.Song
import ru.stersh.youamp.core.api.provider.ApiProvider

suspend fun Song.toMediaItem(apiProvider: ApiProvider): MediaItem {
    val songUri = apiProvider
        .getApi()
        .streamUrl(id)
        .toUri()

    val artworkUri = apiProvider
        .getApi()
        .getCoverArtUrl(coverArt, auth = true)
        ?.toUri()

    val songRating = userRating
    val rating = if (songRating != null && songRating > 0) {
        StarRating(5, songRating.toFloat())
    } else {
        StarRating(5)
    }
    val starredRating = HeartRating(starred != null)

    val metadata = MediaMetadata
        .Builder()
        .setTitle(title)
        .setArtist(artist)
        .setIsPlayable(true)
        .setIsBrowsable(false)
        .setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
        .setOverallRating(rating)
        .setUserRating(starredRating)
        .setArtworkUri(artworkUri)
        .build()
    return MediaItem
        .Builder()
        .setMediaId(id)
        .setMediaMetadata(metadata)
        .setUri(songUri)
        .build()
}
