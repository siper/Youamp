package ru.stersh.youamp.feature.server.create.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ServerUi(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val useLegacyAuth: Boolean
)
