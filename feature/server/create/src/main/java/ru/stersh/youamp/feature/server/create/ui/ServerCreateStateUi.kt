package ru.stersh.youamp.feature.server.create.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ServerCreateStateUi(
    val serverInfo: ServerInfoUi = ServerInfoUi(),
    val progress: Boolean = true,
    val closeAvailable: Boolean = false,
    val buttonsEnabled: Boolean = false
)

@Immutable
internal data class ServerInfoUi(
    val name: String = "",
    val url: String = "http://",
    val username: String = "",
    val password: String = "",
    val useLegacyAuth: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotEmpty() && url.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
}