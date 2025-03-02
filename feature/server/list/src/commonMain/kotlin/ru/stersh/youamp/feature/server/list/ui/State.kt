package ru.stersh.youamp.feature.server.list.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class StateUi(
    val progress: Boolean = true,
    val items: ImmutableList<ServerUi> = persistentListOf()
)

internal data class ServerUi(
    val id: Long,
    val title: String,
    val url: String,
    val isActive: Boolean
)
