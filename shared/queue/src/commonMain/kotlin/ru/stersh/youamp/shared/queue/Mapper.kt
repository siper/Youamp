package ru.stersh.youamp.shared.queue

import ru.stersh.subsonic.api.model.Song
import ru.stersh.youamp.core.api.ApiProvider

suspend fun Song.toMediaItem(apiProvider: ApiProvider): ru.stersh.youamp.core.player.MediaItem {
    val songUri =
        apiProvider
            .getApi()
            .streamUrl(id)

    val artworkUri =
        apiProvider
            .getApi()
            .getCoverArtUrl(
                coverArt,
                auth = true,
            )

    return ru.stersh.youamp.core.player.MediaItem(
        id = id,
        title = title,
        url = songUri,
        artist = artist,
        album = album,
        artworkUrl = artworkUri,
    )
}
