package ru.stresh.youamp.shared.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stresh.youamp.core.api.ApiProvider
import java.util.concurrent.ConcurrentHashMap

internal class FavoritesStorageImpl(
    private val apiProvider: ApiProvider
) : SongFavoritesStorage, AlbumFavoritesStorage, ArtistFavoritesStorage {

    private val cache = ConcurrentHashMap<Long, Favorites>()
    private val locks = ConcurrentHashMap<Long, Mutex>()

    override fun flowSongs(): Flow<List<Song>> {
        return apiProvider
            .flowApiId()
            .filterNotNull()
            .onEach { updateFavoritesIfNeed(it) }
            .flatMapLatest { getFavorites(it).songs() }
    }

    override suspend fun getSongs(): List<Song> {
        return flowSongs().first()
    }

    override suspend fun setSongFavorite(id: String, isFavorite: Boolean) {
        val apiId = apiProvider.getApiId() ?: return
        val api = apiProvider.getApi(apiId) ?: return
        getLock(apiId).withLock {
            if (isFavorite) {
                api.star(id = arrayListOf(id))
            } else {
                api.unstar(id = arrayListOf(id))
            }
            updateFavorites(apiId)
        }
    }

    override fun flowAlbums(): Flow<List<Album>> {
        return apiProvider
            .flowApiId()
            .filterNotNull()
            .onEach { updateFavoritesIfNeed(it) }
            .flatMapLatest { getFavorites(it).albums() }
    }

    override suspend fun getAlbums(): List<Album> {
        return flowAlbums().first()
    }

    override suspend fun setAlbumFavorite(id: String, isFavorite: Boolean) {
        val apiId = apiProvider.getApiId() ?: return
        val api = apiProvider.getApi(apiId) ?: return
        getLock(apiId).withLock {
            if (isFavorite) {
                api.star(albumId = arrayListOf(id))
            } else {
                api.unstar(albumId = arrayListOf(id))
            }
            updateFavorites(apiId)
        }
    }

    override fun flowArtists(): Flow<List<Artist>> {
        return apiProvider
            .flowApiId()
            .filterNotNull()
            .onEach { updateFavoritesIfNeed(it) }
            .flatMapLatest { getFavorites(it).artists() }
    }

    override suspend fun getArtists(): List<Artist> {
        return flowArtists().first()
    }

    override suspend fun setArtistFavorite(id: String, isFavorite: Boolean) {
        val apiId = apiProvider.getApiId() ?: return
        val api = apiProvider.getApi(apiId) ?: return
        getLock(apiId).withLock {
            if (isFavorite) {
                api.star(artistId = arrayListOf(id))
            } else {
                api.unstar(artistId = arrayListOf(id))
            }
            updateFavorites(apiId)
        }
    }

    private fun getLock(id: Long): Mutex {
        return locks.getOrPut(id) { Mutex() }
    }

    private fun getFavorites(id: Long): Favorites {
        return cache.getOrPut(id) { Favorites() }
    }

    private suspend fun updateFavoritesIfNeed(id: Long) {
        val favorites = getFavorites(id)
        if (favorites.needFetchData) {
            getLock(id).withLock {
                if (favorites.needFetchData) {
                    updateFavorites(id)
                }
            }
        }
    }

    private suspend fun updateFavorites(id: Long) {
        val api = apiProvider.requireApi(id)
        val newFavorites = api.getStarred2()
        getFavorites(id).update(
            songs = newFavorites.data.starred2.song?.map {
                Song(
                    id = it.id,
                    title = it.title,
                    album = it.album,
                    albumId = it.albumId,
                    artist = it.artist,
                    artistId = it.artistId,
                    artworkUrl = api.getCoverArtUrl(it.coverArt),
                    userRating = it.userRating
                )
            }.orEmpty(),
            albums = newFavorites.data.starred2.album?.map {
                Album(
                    id = it.id,
                    title = requireNotNull(it.name ?: it.album),
                    artist = it.artist,
                    artistId = it.artistId,
                    artworkUrl = api.getCoverArtUrl(it.coverArt),
                    userRating = it.userRating
                )
            }.orEmpty(),
            artists = newFavorites.data.starred2.artist?.map {
                Artist(
                    id = it.id,
                    name = it.name,
                    artworkUrl = api.getCoverArtUrl(it.coverArt),
                    userRating = it.userRating
                )
            }.orEmpty()
        )
    }
}