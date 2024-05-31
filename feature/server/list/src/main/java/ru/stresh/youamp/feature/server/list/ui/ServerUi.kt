package ru.stresh.youamp.feature.server.list.ui

import androidx.compose.runtime.Immutable

@Immutable
data class ServerUi(
    val id: Long,
    val title: String,
    val url: String,
    val isActive: Boolean
)
