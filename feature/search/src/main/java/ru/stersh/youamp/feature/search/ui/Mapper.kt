package ru.stersh.youamp.feature.search.ui

import ru.stersh.youamp.feature.search.domain.SearchResult

internal fun SearchResult.Song.toUi(): SearchResultUi.Song {
    return SearchResultUi.Song(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}

internal fun SearchResult.Album.toUi(): SearchResultUi.Album {
    return SearchResultUi.Album(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = artworkUrl
    )
}

internal fun SearchResult.Artist.toUi(): SearchResultUi.Artist {
    return SearchResultUi.Artist(
        id = id,
        name = name,
        artworkUrl = artworkUrl
    )
}