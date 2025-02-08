package ru.stresh.youamp.feature.explore.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayingSource
import ru.stersh.youamp.shared.player.state.PlayStateStore
import ru.stresh.youamp.feature.explore.domain.Explore
import ru.stresh.youamp.feature.explore.domain.ExploreRepository
import ru.stresh.youamp.feature.explore.domain.Song
import ru.stresh.youamp.shared.song.random.SongRandomStorage

internal class ExploreRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val queueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playStateStore: PlayStateStore,
    private val songRandomStorage: SongRandomStorage
) : ExploreRepository {
    override fun getExplore(): Flow<Explore> {
        return combine(
            apiProvider
                .flowApiId()
                .filterNotNull(),
            songRandomStorage
                .flowSongs(),
            queueAudioSourceManager
                .playingSource()
                .flatMapLatest { source ->
                    playStateStore
                        .isPlaying()
                        .map { isPlaying ->
                            source.takeIf { isPlaying }
                        }
                }
        ) { apiId, randomSongs, playingSource ->
            return@combine Explore(
                randomSongs = randomSongs.map { song ->
                    Song(
                        id = song.id,
                        title = song.title,
                        artist = song.artist,
                        artworkUrl = song.artworkUrl,
                        isPlaying = playingSource?.isSongPlaying(apiId, song.id) == true
                    )
                }
            )
        }
    }

    override suspend fun refresh() {
        songRandomStorage.refresh()
    }

    private fun PlayingSource?.isSongPlaying(serverId: Long, songId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == songId && this.type == PlayingSource.Type.Song
    }
}