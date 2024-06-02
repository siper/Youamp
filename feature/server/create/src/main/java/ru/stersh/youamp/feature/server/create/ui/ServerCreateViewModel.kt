package ru.stersh.youamp.feature.server.create.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.server.create.domain.ServerRepository

internal class ServerCreateViewModel(
    private val serverId: Long?,
    private val serverRepository: ServerRepository
) : ViewModel() {
    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    private val _exit = Channel<Unit>()
    val exit: Flow<Unit>
        get() = _exit.receiveAsFlow()

    private val _testResult = Channel<ServerTestResultUi>()
    val testResult: Flow<ServerTestResultUi>
        get() = _testResult.receiveAsFlow()

    init {
        viewModelScope.launch {
            val returnAvailable = serverRepository.hasActiveServer()

            _state.value = if (serverId != null) {
                val initWith = serverRepository
                    .getServer(serverId)
                    ?.toUi()
                StateUi.Content(
                    buttonsEnabled = initWith != null,
                    returnAvailable = returnAvailable,
                    initWith = initWith
                )
            } else {
                StateUi.Content(
                    buttonsEnabled = false,
                    returnAvailable = returnAvailable,
                    initWith = null
                )
            }
        }
    }

    fun add(server: ServerUi) = viewModelScope.launch {
        if (serverId != null) {
            serverRepository.editServer(serverId, server.toDomain())
        } else {
            serverRepository.addServer(server.toDomain())
        }
        _exit.trySend(Unit)
    }

    fun test(server: ServerUi) = viewModelScope.launch {
        runCatching { serverRepository.testServer(server.toDomain()) }.fold(
            onSuccess = {
                _testResult.send(ServerTestResultUi.SUCCESS)
            },
            onFailure = {
                val t = it
                Log.d("Error", "Error", it)
                _testResult.send(ServerTestResultUi.ERROR)
            }
        )
    }

    fun validateInput(input: ServerInputUi) {
        val contentState = _state.value as? StateUi.Content ?: return

        _state.value = contentState.copy(buttonsEnabled = input.isValid)
    }
}