package ru.stersh.youamp.feature.album.info.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.album.info.domain.AlbumFavoriteRepository
import ru.stersh.youamp.feature.album.info.domain.AlbumInfoRepository
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class AlbumInfoViewModel(
    private val id: String,
    private val albumInfoRepository: AlbumInfoRepository,
    private val albumFavoriteRepository: AlbumFavoriteRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
) : ViewModel() {
    private val _state = MutableStateFlow(AlbumInfoStateUi())
    val state: StateFlow<AlbumInfoStateUi>
        get() = _state

    init {
        loadAlbum()
    }

    fun playShuffled() =
        viewModelScope.launch {
            playerQueueAudioSourceManager.playSource(
                AudioSource.Album(id),
                shuffled = true,
            )
        }

    fun playAll() =
        viewModelScope.launch {
            playerQueueAudioSourceManager.playSource(AudioSource.Album(id))
        }

    fun retry() {
        loadAlbum()
    }

    fun onFavoriteChange(isFavorite: Boolean) =
        viewModelScope.launch {
            runCatching {
                albumFavoriteRepository.setFavorite(
                    id,
                    isFavorite,
                )
            }.fold(
                onSuccess = {
                    _state.update {
                        it.copy(content = it.content?.copy(isFavorite = isFavorite))
                    }
                },
                onFailure = {
                    Logger.w(it) { "Error album favorite change" }
                },
            )
        }

    private fun loadAlbum() =
        viewModelScope.launch {
            _state.update {
                it.copy(
                    progress = true,
                    error = false,
                    content = null,
                )
            }
            runCatching { albumInfoRepository.getAlbumInfo(id) }.fold(
                onSuccess = { albumInfo ->
                    _state.update {
                        it.copy(
                            progress = false,
                            error = false,
                            content = albumInfo.toUi(),
                        )
                    }
                },
                onFailure = { throwable ->
                    Logger.w(throwable) { "Error load album" }
                    _state.update {
                        it.copy(
                            progress = false,
                            error = true,
                            content = null,
                        )
                    }
                },
            )
        }
}
