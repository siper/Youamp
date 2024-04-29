package ru.stresh.youamp.main.ui

internal sealed interface StateUi {
    data object Content : StateUi
    data object CreateNewServer : StateUi
    data object Progress : StateUi
}