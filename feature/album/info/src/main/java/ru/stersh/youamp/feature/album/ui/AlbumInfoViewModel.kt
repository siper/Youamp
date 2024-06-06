package ru.stersh.youamp.feature.album.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.album.domain.AlbumInfoRepository
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import timber.log.Timber

internal class AlbumInfoViewModel(
    private val id: String,
    private val albumInfoRepository: AlbumInfoRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager
) : ViewModel() {

    private val _state = MutableStateFlow(AlbumInfoStateUi())
    val state: StateFlow<AlbumInfoStateUi>
        get() = _state

    init {
        loadAlbum()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Album(id), true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Album(id))
    }

    fun retry() {
        loadAlbum()
    }

    private fun loadAlbum() = viewModelScope.launch {
        _state.update {
            it.copy(
                progress = true,
                error = false,
                content = null
            )
        }
        runCatching { albumInfoRepository.getAlbumInfo(id) }.fold(
            onSuccess = { albumInfo ->
                _state.update {
                    it.copy(
                        progress = false,
                        error = false,
                        content = albumInfo.toUi()
                    )
                }
            },
            onFailure = { throwable ->
                Timber.w(throwable)
                _state.update {
                    it.copy(
                        progress = false,
                        error = true,
                        content = null
                    )
                }
            }
        )
    }
}