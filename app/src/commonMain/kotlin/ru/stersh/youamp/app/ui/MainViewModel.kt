package ru.stersh.youamp.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.stersh.youamp.app.domain.AvatarUrlRepository
import ru.stersh.youamp.app.domain.ServerExistRepository

internal class MainViewModel(
    private val serverExistRepository: ServerExistRepository,
    private val avatarUrlRepository: AvatarUrlRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                serverExistRepository.hasServer(),
                avatarUrlRepository.getAvatarUrl()
            ) { hasServer, avatarUrl ->
                return@combine StateUi(
                    screen = if (hasServer) {
                        MainScreen.Main
                    } else {
                        MainScreen.AddServer
                    },
                    avatarUrl = avatarUrl
                )
            }
                .collect {
                    _state.value = it
                }
        }
    }
}