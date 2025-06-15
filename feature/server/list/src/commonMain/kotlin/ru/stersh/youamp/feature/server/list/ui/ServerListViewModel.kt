package ru.stersh.youamp.feature.server.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.server.list.domain.ServerListRepository

internal class ServerListViewModel(
    private val serverListRepository: ServerListRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            serverListRepository
                .getServerList()
                .mapItems { it.toUi() }
                .map { it.toPersistentList() }
                .flowOn(Dispatchers.IO)
                .collect { servers ->
                    _state.update {
                        it.copy(
                            progress = false,
                            items = servers,
                        )
                    }
                }
        }
    }

    fun deleteServer(serverId: Long) =
        viewModelScope.launch {
            serverListRepository.deleteServer(serverId)
        }

    fun setActiveServer(serverId: Long) =
        viewModelScope.launch {
            serverListRepository.setActiveServer(serverId)
        }
}
