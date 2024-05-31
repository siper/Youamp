package ru.stresh.youamp.feature.server.create.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ServerInputUi(
    val name: String,
    val url: String,
    val username: String,
    val password: String
) {
    val isValid: Boolean
        get() = name.isNotEmpty() && url.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
}