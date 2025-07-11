package ru.stersh.youamp.feature.search.data

import ru.stersh.subsonic.api.SubsonicApi
import ru.stersh.subsonic.api.model.Album
import ru.stersh.subsonic.api.model.Artist
import ru.stersh.subsonic.api.model.Song
import ru.stersh.youamp.feature.search.domain.SearchResult

internal fun List<Song>?.toDomainSongs(api: SubsonicApi): List<SearchResult.Song> =
    this
        ?.filter { it.isDir != true && it.isVideo != true }
        ?.map { it.toDomain(api) }
        .orEmpty()

internal fun List<Album>?.toDomainAlbums(api: SubsonicApi): List<SearchResult.Album> =
    this
        ?.map { it.toDomain(api) }
        .orEmpty()

internal fun List<Artist>?.toDomainArtists(api: SubsonicApi): List<SearchResult.Artist> =
    this
        ?.map { it.toDomain(api) }
        .orEmpty()

internal fun Song.toDomain(api: SubsonicApi): SearchResult.Song =
    SearchResult.Song(
        id = id,
        title = title,
        artist = artist,
        artworkUrl = api.getCoverArtUrl(coverArt),
    )

internal fun Album.toDomain(api: SubsonicApi): SearchResult.Album =
    SearchResult.Album(
        id = id,
        title = name.orEmpty(),
        artist = artist,
        artworkUrl = api.getCoverArtUrl(coverArt),
    )

internal fun Artist.toDomain(api: SubsonicApi): SearchResult.Artist =
    SearchResult.Artist(
        id = id,
        name = name,
        artworkUrl = api.getCoverArtUrl(coverArt),
    )
