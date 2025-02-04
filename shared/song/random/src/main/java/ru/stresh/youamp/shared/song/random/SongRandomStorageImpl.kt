package ru.stresh.youamp.shared.song.random

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.api.provider.ApiProvider
import java.util.concurrent.ConcurrentHashMap

internal class SongRandomStorageImpl(
    private val apiProvider: ApiProvider
) : SongRandomStorage {

    private val cache = ConcurrentHashMap<Long, RandomSongs>()
    private val locks = ConcurrentHashMap<Long, Mutex>()

    override fun flowSongs(): Flow<List<Song>> {
        return apiProvider
            .flowApiId()
            .filterNotNull()
            .onEach { updateRandomSongsIfNeed(it) }
            .flatMapLatest { getRandomSongs(it).songs() }
    }

    override suspend fun getSongs(): List<Song> {
        return flowSongs().first()
    }

    override suspend fun refresh() {
        val apiId = apiProvider.getApiId() ?: return
        getLock(apiId).withLock {
            updateRandomSongs(apiId)
        }
    }

    private fun getLock(id: Long): Mutex {
        return locks.getOrPut(id) { Mutex() }
    }

    private fun getRandomSongs(id: Long): RandomSongs {
        return cache.getOrPut(id) { RandomSongs() }
    }

    private suspend fun updateRandomSongsIfNeed(id: Long) {
        val randomSongs = getRandomSongs(id)
        if (randomSongs.needFetchData) {
            getLock(id).withLock {
                if (randomSongs.needFetchData) {
                    updateRandomSongs(id)
                }
            }
        }
    }

    private suspend fun updateRandomSongs(id: Long) {
        val api = apiProvider.requireApi(id)
        val newRandomSongs = api.getRandomSongs(30)
        getRandomSongs(id).update(
            songs = newRandomSongs.map {
                Song(
                    id = it.id,
                    title = it.title,
                    album = it.album,
                    albumId = it.albumId,
                    artist = it.artist,
                    artistId = it.artistId,
                    artworkUrl = api.getCoverArtUrl(it.coverArt)
                )
            }
        )
    }
}