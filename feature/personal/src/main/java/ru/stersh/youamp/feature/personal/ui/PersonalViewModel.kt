package ru.stersh.youamp.feature.personal.ui

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
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.personal.domain.PersonalRepository
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.equals
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PersonalViewModel(
    private val personalRepository: PersonalRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playStateStore: PlayStateStore,
    private val playerControls: PlayerControls,
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
            personalRepository
                .getPersonal()
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch {
                    _state.update {
                        it.copy(
                            progress = false,
                            refreshing = false,
                            error = true
                        )
                    }
                }
                .collect { personal ->
                    _state.update {
                        it.copy(
                            progress = false,
                            refreshing = false,
                            data = personal
                        )
                    }
                }
        }
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

    fun refresh() {
        _state.update {
            it.copy(refreshing = true)
        }
        stateJob?.cancel()
        subscribeState()
    }

    fun onPlayPauseAudioSource(source: AudioSource) {
        viewModelScope.launch {
            val playingSource = playerQueueAudioSourceManager
                .playingSource()
                .first()
            val serverId = apiProvider.requireApiId()
            val isPlaying = playStateStore
                .isPlaying()
                .first()
            val isSourcePlaying = playingSource?.equals(
                serverId,
                source
            ) == true
            if (isSourcePlaying && isPlaying) {
                playerControls.pause()
                return@launch
            }
            if (isSourcePlaying) {
                playerControls.play()
                return@launch
            }
            playerQueueAudioSourceManager.playSource(source)
        }
    }
}