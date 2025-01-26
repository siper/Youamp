package ru.stersh.youamp.feature.artist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.artist.domain.ArtistInfoRepository
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import timber.log.Timber

internal class ArtistInfoViewModel(
    private val id: String,
    private val artistInfoRepository: ArtistInfoRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager
) : ViewModel() {

    private val _state = MutableStateFlow(ArtistInfoStateUi())
    val state: StateFlow<ArtistInfoStateUi>
        get() = _state

    init {
        loadArtist()
    }

    fun playShuffled() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Artist(id), shuffled = true)
    }

    fun playAll() = viewModelScope.launch {
        playerQueueAudioSourceManager.playSource(AudioSource.Artist(id))
    }

    fun retry() {
        loadArtist()
    }

    private fun loadArtist() = viewModelScope.launch {
        _state.update {
            it.copy(
                progress = true,
                error = false,
                content = null
            )
        }

        runCatching { artistInfoRepository.getArtistInfo(id) }.fold(
            onSuccess = { artistInfo ->
                _state.update {
                    it.copy(
                        progress = false,
                        error = false,
                        content = artistInfo.toUi()
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