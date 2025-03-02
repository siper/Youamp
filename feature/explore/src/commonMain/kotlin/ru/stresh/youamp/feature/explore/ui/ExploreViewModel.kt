package ru.stresh.youamp.feature.explore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
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
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.player.Player
import ru.stresh.youamp.feature.explore.domain.ExploreRepository
import ru.stresh.youamp.shared.queue.AudioSource
import ru.stresh.youamp.shared.queue.PlayerQueueAudioSourceManager
import ru.stresh.youamp.shared.queue.equals

internal class ExploreViewModel(
    private val repository: ExploreRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val player: Player,
    private val apiProvider: ApiProvider
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    private var stateJob: Job? = null

    init {
        subscribeState()
    }

    private fun subscribeState() {
        stateJob = viewModelScope.launch {
            repository
                .getExplore()
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch {
                    _state.update {
                        it.copy(
                            refreshing = false,
                            progress = false,
                            error = true
                        )
                    }
                }
                .collect { explore ->
                    _state.update {
                        it.copy(
                            refreshing = false,
                            progress = false,
                            data = explore
                        )
                    }
                }
        }
    }

    fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(refreshing = true)
        }
        runCatching { repository.refresh() }
            .onFailure { Logger.w(it) { "Filed to refresh" } }
        stateJob?.cancel()
        subscribeState()
    }

    fun retry() {
        stateJob?.cancel()
        _state.update {
            it.copy(
                progress = true,
                error = false,
                data = null
            )
        }
        subscribeState()
    }

    fun onPlayPauseAudioSource(source: AudioSource) {
        viewModelScope.launch {
            val playingSource = playerQueueAudioSourceManager
                .playingSource()
                .first()
            val serverId = apiProvider.requireApiId()
            val isPlaying = player
                .getIsPlaying()
                .first()
            val isSourcePlaying = playingSource?.equals(
                serverId,
                source
            ) == true
            if (isSourcePlaying && isPlaying) {
                player.pause()
                return@launch
            }
            if (isSourcePlaying) {
                player.play()
                return@launch
            }
            playerQueueAudioSourceManager.playSource(source)
        }
    }
}