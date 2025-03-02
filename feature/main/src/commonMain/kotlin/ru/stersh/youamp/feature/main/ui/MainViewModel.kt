package ru.stersh.youamp.feature.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.main.domain.ServerInfoRepository

internal class MainViewModel(
    private val serverInfoRepository: ServerInfoRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MainStateUi())

    val state: StateFlow<MainStateUi>
        get() = _state

    init {
        viewModelScope.launch {
            serverInfoRepository.getServerInfo().collect { serverInfo ->
                _state.update {
                    it.copy(
                        serverInfo = MainStateUi.ServerInfo(
                            name = serverInfo.name,
                            avatarUrl = serverInfo.avatarUrl
                        )
                    )
                }
            }
        }
    }
}