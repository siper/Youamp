package ru.stresh.youamp.feature.server.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.stresh.youamp.feature.server.list.domain.ServerListRepository

internal class ServerListViewModel(
    private val serverListRepository: ServerListRepository
) : ViewModel() {
    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            serverListRepository
                .getServerList()
                .map { servers ->
                    StateUi.Content(
                        items = servers.map {
                            ServerUi(
                                id = it.id,
                                title = it.title,
                                url = it.url,
                                isActive = it.isActive
                            )
                        }
                    )
                }
                .collect {
                    _state.value = it
                }
        }
    }

    fun deleteServer(serverId: Long) = viewModelScope.launch {
        serverListRepository.deleteServer(serverId)
    }

    fun setActiveServer(serverId: Long) = viewModelScope.launch {
        serverListRepository.setActiveServer(serverId)
    }

    sealed interface StateUi {
        data class Content(val items: List<ServerUi>) : StateUi
        data object Progress : StateUi
    }
}