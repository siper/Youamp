package ru.stersh.youamp.feature.server.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.server.create.domain.ServerRepository

internal class ServerCreateViewModel(
    private val serverId: Long?,
    private val serverRepository: ServerRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ServerCreateStateUi())
    val state: StateFlow<ServerCreateStateUi>
        get() = _state

    private val _exit = Channel<Unit>()
    val exit: Flow<Unit>
        get() = _exit.receiveAsFlow()

    private val _testResult = Channel<ServerTestResultUi>()
    val testResult: Flow<ServerTestResultUi>
        get() = _testResult.receiveAsFlow()

    init {
        viewModelScope.launch {
            val closeAvailable = serverRepository.hasActiveServer()
            if (serverId != null) {
                val server = serverRepository.getServer(serverId)
                val serverInfo = server?.toInfo()
                _state.update {
                    it.copy(
                        serverInfo = serverInfo ?: ServerInfoUi(),
                        progress = false,
                        buttonsEnabled = serverInfo?.isValid == true,
                        closeAvailable = closeAvailable
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        buttonsEnabled = false,
                        progress = false,
                        closeAvailable = closeAvailable
                    )
                }
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
                _testResult.send(ServerTestResultUi.Success)
            },
            onFailure = {
                Logger.w(it) { "Filed to test server" }
                _testResult.send(ServerTestResultUi.Error)
            }
        )
    }

    fun validateInput(input: ServerInputUi) {
        _state.update {
            it.copy(
                serverInfo = it.serverInfo.copy(
                    name = input.name,
                    url = input.url,
                    username = input.username,
                    password = input.password
                ),
                buttonsEnabled = input.isValid
            )
        }
    }
}