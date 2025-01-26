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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.playlist.domain.PlaylistInfoRepository
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.state.PlayStateStore
import timber.log.Timber

internal class PlaylistInfoViewModel(
    private val id: String,
    private val playlistInfoRepository: PlaylistInfoRepository,
    private val playerStateStore: PlayStateStore,
    private val currentSongInfoStore: CurrentSongInfoStore,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playerControls: PlayerControls,
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
                            playlistInfo = playlistInfo.toUi()
                        )
                    }
                }
        }
    }
}