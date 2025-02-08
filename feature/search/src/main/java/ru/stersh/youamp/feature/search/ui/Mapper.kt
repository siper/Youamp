package ru.stersh.youamp.feature.search.ui

import ru.stersh.youamp.feature.search.domain.SearchResult

internal fun SearchResult.Song.toUi(): Song {
    return Song(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}

internal fun SearchResult.Album.toUi(): Album {
    return Album(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}

internal fun SearchResult.Artist.toUi(): Artist {
    return Artist(
        id = id,
        name = name,
        artworkUrl = artworkUrl
    )
}