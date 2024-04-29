package ru.stresh.youamp.feature.server.create.ui

internal data class ServerUi(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val useLegacyAuth: Boolean
)
