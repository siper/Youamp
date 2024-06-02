package ru.stersh.youamp.feature.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PlaylistInfoViewModel(
    private val id: String,
    private val apiProvider: ApiProvider,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playerStateStore: PlayStateStore,
    private val currentSongInfoStore: CurrentSongInfoStore,
    private val playerControls: PlayerControls
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistInfoScreenStateUi())
    val state: StateFlow<PlaylistInfoScreenStateUi>
        get() = _state

    init {
        loadPlaylist()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id), true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id))
    }

    fun onPlaySong(songId: String) = viewModelScope.launch {
        val currentSongInfo = currentSongInfoStore
            .getCurrentSongInfo()
            .first()
        if (currentSongInfo == null || currentSongInfo.id != songId) {
            playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id, songId))
            return@launch
        }
        val isPlaying = playerStateStore
            .isPlaying()
            .first()
        if (isPlaying) {
            playerControls.pause()
        } else {
            playerControls.play()
        }
    }

    private fun loadPlaylist() = viewModelScope.launch {
        val api = apiProvider.getApi()

        flow {
            emit(api.getPlaylist(id))
        }
            .flatMapLatest { playlist ->
                combine(
                    currentSongInfoStore.getCurrentSongInfo(),
                    playerStateStore.isPlaying()
                ) { currentSongInfo, isPlaying ->
                    return@combine PlaylistInfoScreenStateUi(
                        progress = false,
                        title = playlist.name,
                        artworkUrl = api.getCoverArtUrl(playlist.coverArt, auth = false),
                        songs = playlist
                            .entry
                            .orEmpty()
                            .map { entry ->
                                val isCurrent = currentSongInfo?.id == entry.id
                                PlaylistSongUi(
                                    id = entry.id,
                                    title = entry.title,
                                    artist = entry.artist,
                                    artworkUrl = entry.coverArt?.let { api.getCoverArtUrl(it) },
                                    isCurrent = isCurrent,
                                    isPlaying = isCurrent && isPlaying
                                )
                            }
                    )
                }
            }
            .flowOn(Dispatchers.IO)
            .collect { newState ->
                _state.update {
                    it.copy(
                        progress = newState.progress,
                        title = newState.title,
                        artworkUrl = newState.artworkUrl,
                        songs = newState.songs
                    )
                }
            }
    }
}