package ru.stersh.youamp.feature.server.create.ui

internal data class ServerInputUi(
    val name: String,
    val url: String,
    val username: String,
    val password: String,
) {
    val isValid: Boolean =
        name.isNotEmpty() && url.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
}
