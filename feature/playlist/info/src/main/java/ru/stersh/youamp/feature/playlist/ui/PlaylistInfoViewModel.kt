package ru.stersh.youamp.feature.playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.playlist.domain.PlaylistInfoRepository
import ru.stresh.youamp.core.player.Player
import ru.stresh.youamp.shared.queue.AudioSource
import ru.stresh.youamp.shared.queue.PlayerQueueAudioSourceManager
import timber.log.Timber

internal class PlaylistInfoViewModel(
    private val id: String,
    private val playlistInfoRepository: PlaylistInfoRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val player: Player
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistInfoScreenStateUi())
    val state: StateFlow<PlaylistInfoScreenStateUi>
        get() = _state

    private var playlistInfoJob: Job? = null

    init {
        loadPlaylist()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id), shuffled = true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id))
    }

    fun onPlaySong(songId: String) = viewModelScope.launch {
        val currentMediaItem = player
            .getCurrentMediaItem()
            .first()
        if (currentMediaItem == null || currentMediaItem.id != songId) {
            playerQueueAudioSourceManager.playSource(AudioSource.Playlist(id, songId))
            return@launch
        }
        val isPlaying = player
            .getIsPlaying()
            .first()
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun retry() {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        playlistInfoJob?.cancel()
        _state.update {
            it.copy(
                playlistInfo = null,
                progress = true,
                error = false
            )
        }
        playlistInfoJob = viewModelScope.launch {
            playlistInfoRepository
                .getPlaylistInfo(id)
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    Timber.w(throwable)
                    _state.update {
                        it.copy(
                            progress = false,
                            error = true
                        )
                    }
                }
                .collect { playlistInfo ->
                    _state.update {
                        it.copy(
                            progress = false,
                            error = false,
                            playlistInfo = playlistInfo
                        )
                    }
                }
        }
    }
}