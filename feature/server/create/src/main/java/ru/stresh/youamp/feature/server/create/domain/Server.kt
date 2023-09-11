package ru.stresh.youamp.feature.server.create.domain

internal data class Server(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val useLegacyAuth: Boolean
)
