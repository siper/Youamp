package ru.stersh.youamp.feature.search.domain

internal data class SearchResult(
    val songs: List<Song>,
    val albums: List<Album>,
    val artists: List<Artist>,
    val hasMoreSongs: Boolean,
    val hasMoreAlbums: Boolean,
    val hasMoreArtists: Boolean,
) {

    data class Song(
        val id: String,
        val title: String,
        val artist: String,
        val artworkUrl: String?
    )

    data class Album(
        val id: String,
        val title: String,
        val artist: String,
        val artworkUrl: String?
    )

    data class Artist(
        val id: String,
        val name: String,
        val artworkUrl: String?
    )

    companion object {

        val Empty = SearchResult(
            songs = emptyList(),
            albums = emptyList(),
            artists = emptyList(),
            hasMoreSongs = false,
            hasMoreAlbums = false,
            hasMoreArtists = false
        )
    }
}