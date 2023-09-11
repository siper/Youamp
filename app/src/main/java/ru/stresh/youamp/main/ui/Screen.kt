package ru.stresh.youamp.main.ui

sealed class Screen(val destination: String) {
    data object Main : Screen("main")
    data object AddServer : Screen("add_server")

}