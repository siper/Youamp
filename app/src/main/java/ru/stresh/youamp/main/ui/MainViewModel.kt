package ru.stresh.youamp.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.stresh.youamp.main.domain.ServerExistRepository

internal class MainViewModel(
    private val serverExistRepository: ServerExistRepository
) : ViewModel() {

    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        checkServer()
    }

    private fun checkServer() = viewModelScope.launch {
        _state.value = if (serverExistRepository.hasServer()) {
            StateUi.Content
        } else {
            StateUi.CreateNewServer
        }
    }
}