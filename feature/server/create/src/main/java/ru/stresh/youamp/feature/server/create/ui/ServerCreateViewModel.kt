package ru.stresh.youamp.feature.server.create.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.stresh.youamp.feature.server.create.domain.Server
import ru.stresh.youamp.feature.server.create.domain.ServerRepository

internal class ServerCreateViewModel(
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _buttonsEnabled = MutableStateFlow(false)
    val buttonsEnabled: StateFlow<Boolean>
        get() = _buttonsEnabled

    private val _returnAvailable = MutableStateFlow(true)
    val returnAvailable: StateFlow<Boolean>
        get() = _returnAvailable

    private val _exit = Channel<Unit>()
    val exit: Flow<Unit>
        get() = _exit.receiveAsFlow()

    fun add(
        name: String,
        url: String,
        username: String,
        password: String,
        useLegacyAuth: Boolean
    ) = viewModelScope.launch {
        val server = Server(name, url, username, password, useLegacyAuth)
        serverRepository.addServer(server)
        _exit.trySend(Unit)
    }

    fun test(
        server: String,
        username: String,
        password: String,
        useLegacyAuth: Boolean
    ) = viewModelScope.launch {

    }

    fun checkValues(
        name: String,
        url: String,
        username: String,
        password: String
    ) {
        _buttonsEnabled.value = name.isNotEmpty() && url.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
    }
}