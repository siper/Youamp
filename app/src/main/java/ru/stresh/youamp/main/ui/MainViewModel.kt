package ru.stresh.youamp.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.stresh.youamp.main.domain.ServerExistRepository

internal class MainViewModel(
    private val serverExistRepository: ServerExistRepository
) : ViewModel() {

    private val _navigation = Channel<Screen>()
    val navigation: Flow<Screen>
        get() = _navigation.receiveAsFlow()

    init {
        checkServer()
    }

    private fun checkServer() = viewModelScope.launch {
        val screen = if (serverExistRepository.hasServer()) {
            Screen.Main
        } else {
            Screen.AddServer
        }
        _navigation.send(screen)
    }
}