package ru.stresh.youamp.feature.explore.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayingSource
import ru.stersh.youamp.shared.player.state.PlayStateStore
import ru.stresh.youamp.feature.explore.domain.Explore
import ru.stresh.youamp.feature.explore.domain.ExploreRepository
import ru.stresh.youamp.feature.explore.domain.Song

internal class ExploreRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val queueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playStateStore: PlayStateStore
) : ExploreRepository {
    override fun getExplore(): Flow<Explore> {
        return combine(
            apiProvider
                .flowApi()
                .map { api ->
                    ApiSongs(
                        serverId = apiProvider.requireApiId(),
                        api = api,
                        randomSongs = api.getRandomSongs(size = 9)
                    )
                },
            queueAudioSourceManager
                .playingSource()
                .flatMapLatest { source ->
                    playStateStore
                        .isPlaying()
                        .map { isPlaying ->
                            source.takeIf { isPlaying }
                        }
                }
        ) { randomSongs, playingSource ->
            return@combine Explore(
                randomSongs = randomSongs.let {
                    val serverId = it.serverId
                    randomSongs.randomSongs.map { song ->
                        Song(
                            id = song.id,
                            title = song.title,
                            artist = song.artist,
                            artworkUrl = it.api.getCoverArtUrl(song.coverArt),
                            isPlaying = playingSource?.isSongPlaying(serverId, song.id) == true
                        )
                    }
                }
            )
        }
    }

    private data class ApiSongs(
        val serverId: Long,
        val api: SubsonicApi,
        val randomSongs: List<ru.stersh.youamp.core.api.Song>
    )

    private fun PlayingSource?.isSongPlaying(serverId: Long, songId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == songId && this.type == PlayingSource.Type.Song
    }
}