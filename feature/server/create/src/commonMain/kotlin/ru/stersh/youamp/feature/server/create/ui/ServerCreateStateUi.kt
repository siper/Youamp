package ru.stersh.youamp.feature.server.create.ui

internal data class ServerCreateStateUi(
    val serverInfo: ServerInfoUi = ServerInfoUi(),
    val progress: Boolean = true,
    val closeAvailable: Boolean = false,
    val buttonsEnabled: Boolean = false
)

internal data class ServerInfoUi(
    val name: String = "",
    val url: String = "http://",
    val username: String = "",
    val password: String = "",
    val useLegacyAuth: Boolean = false
) {
    val isValid: Boolean = name.isNotEmpty() && url.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
}