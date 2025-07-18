package ru.stersh.youamp.feature.personal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.personal.domain.PersonalRepository

internal class PersonalViewModel(
    private val personalRepository: PersonalRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    private var stateJob: Job? = null

    init {
        subscribeState()
    }

    private fun subscribeState() {
        stateJob =
            viewModelScope.launch {
                personalRepository
                    .getPersonal()
                    .map { it.toUi() }
                    .flowOn(Dispatchers.IO)
                    .catch {
                        Logger.w(it) { "Error subscribing state" }
                        _state.update {
                            it.copy(
                                progress = false,
                                refreshing = false,
                                error = true,
                            )
                        }
                    }.collect { personal ->
                        _state.update {
                            it.copy(
                                progress = false,
                                refreshing = false,
                                data = personal,
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
                data = null,
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
}
