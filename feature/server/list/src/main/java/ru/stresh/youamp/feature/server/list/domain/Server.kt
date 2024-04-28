package ru.stresh.youamp.feature.server.list.domain

internal data class Server(
    val id: Long,
    val title: String,
    val url: String,
    val isActive: Boolean
)
